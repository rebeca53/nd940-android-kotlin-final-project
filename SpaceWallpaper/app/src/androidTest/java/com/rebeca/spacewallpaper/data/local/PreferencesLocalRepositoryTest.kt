package com.rebeca.spacewallpaper.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.rebeca.spacewallpaper.data.local.preferences.PreferencesDTO
import com.rebeca.spacewallpaper.data.local.preferences.PreferencesLocalRepository
import com.rebeca.spacewallpaper.data.local.preferences.PreferencesDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class PreferencesLocalRepositoryTest {
    private lateinit var preferencesRepository: PreferencesLocalRepository
    private lateinit var database: PreferencesDatabase

    @Before
    fun setup() {
        // using an in-memory database for testing, since it doesn't survive killing the process
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            PreferencesDatabase::class.java
        ).allowMainThreadQueries().build()

        preferencesRepository = PreferencesLocalRepository(database.getPreferencesDAO(), Dispatchers.Main)
    }

    @After
    fun cleanup() {
        database.close()
        stopKoin()
    }

    @Test
    fun saveEnableWorker_getEnableWorker() = runBlocking {
        // given true enable worker and get the current value of enable worker as false
        preferencesRepository.savePreferences(PreferencesDTO())
        val previousEnable = preferencesRepository.getEnableWorker()
        preferencesRepository.setEnableWorker(true)

        // WHEN new value is set
        val result = preferencesRepository.getEnableWorker()

        // then
        result as RequestResult.Success
        previousEnable as RequestResult.Success

        assertThat(result.data, `is`(true))
        assertThat(previousEnable.data, `is`(false))
    }

    @Test
    fun savePreferences_updateEachField() = runBlocking {
        // given true enable worker and get the current value of enable worker as false
        val enable = true
        val frequency = 7L
        val hour = 3
        val minute = 3
        val confirm = true
        val preference = PreferencesDTO()
        preferencesRepository.savePreferences(preference)

        // WHEN new value is set
        preferencesRepository.setEnableWorker(enable)
        preferencesRepository.setFrequency(frequency)
        preferencesRepository.setSchedule(hour, minute)
        preferencesRepository.setConfirmBeforeApply(confirm)
        val result = preferencesRepository.getPreferences()

        // then
        result as RequestResult.Success

        assertThat(result.data.enable, `is`(enable))
        assertThat(result.data.enable, not(preference.enable))
        assertThat(result.data.frequency, `is`(frequency))
        assertThat(result.data.frequency, not(preference.frequency))
        assertThat(result.data.hour, `is`(hour))
        assertThat(result.data.hour, not(preference.hour))
        assertThat(result.data.minute, `is`(minute))
        assertThat(result.data.minute, not(preference.minute))
        assertThat(result.data.confirmBeforeApply, `is`(confirm))
        assertThat(result.data.confirmBeforeApply, not(preference.confirmBeforeApply))
    }

    @Test
    fun savePreferences_updatePreferences() = runBlocking {
        val enable = true
        val frequency = 7L
        val hour = 3
        val minute = 3
        val confirm = true
        val newPreference = PreferencesDTO(enable, frequency, hour, minute, confirm)
        val preference = PreferencesDTO()
        preferencesRepository.savePreferences(preference)

        // WHEN new value is set
        preferencesRepository.savePreferences(newPreference)
        val result = preferencesRepository.getPreferences()

        // then
        result as RequestResult.Success

        assertThat(result.data.enable, `is`(newPreference.enable))
        assertThat(result.data.enable, not(preference.enable))
        assertThat(result.data.frequency, `is`(newPreference.frequency))
        assertThat(result.data.frequency, not(preference.frequency))
        assertThat(result.data.hour, `is`(newPreference.hour))
        assertThat(result.data.hour, not(preference.hour))
        assertThat(result.data.minute, `is`(newPreference.minute))
        assertThat(result.data.minute, not(preference.minute))
        assertThat(result.data.confirmBeforeApply, `is`(newPreference.confirmBeforeApply))
        assertThat(result.data.confirmBeforeApply, not(preference.confirmBeforeApply))
    }

    @Test
    fun savePreferences_getEachField() = runBlocking {
        val enable = true
        val frequency = 7L
        val hour = 3
        val minute = 3
        val confirm = true
        val newPreference = PreferencesDTO(enable, frequency, hour, minute, confirm)
        preferencesRepository.savePreferences(newPreference)

        // WHEN new value is set
        val resultEnable = preferencesRepository.getEnableWorker()
        val resultFrequency = preferencesRepository.getFrequency()
        val resultHour = preferencesRepository.getScheduledHour()
        val resultMinute = preferencesRepository.getScheduledMinute()
        val resultConfirm = preferencesRepository.getConfirmBeforeApply()

        // then
        resultEnable as RequestResult.Success
        assertThat(resultEnable.data, `is`(newPreference.enable))
        resultFrequency as RequestResult.Success
        assertThat(resultFrequency.data, `is`(newPreference.frequency))
        resultHour as RequestResult.Success
        assertThat(resultHour.data, `is`(newPreference.hour))
        resultMinute as RequestResult.Success
        assertThat(resultMinute.data, `is`(newPreference.minute))
        resultConfirm as RequestResult.Success
        assertThat(resultConfirm.data, `is`(newPreference.confirmBeforeApply))
    }
}