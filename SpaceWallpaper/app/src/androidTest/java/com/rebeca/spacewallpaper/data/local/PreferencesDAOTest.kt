package com.rebeca.spacewallpaper.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.rebeca.spacewallpaper.data.local.preferences.PreferencesDTO
import com.rebeca.spacewallpaper.data.local.preferences.PreferencesDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class PreferencesDAOTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var preferencesDatabase: PreferencesDatabase

    @Before
    fun initDb() {
        preferencesDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            PreferencesDatabase::class.java
        ).build()
    }

    @After
    fun closeDb() = preferencesDatabase.close()

    @Test
    fun saveEnableWorker_getEnabledWorker() = runBlockingTest {
        // given true enable worker and get the current value of enable worker as false
        preferencesDatabase.getPreferencesDAO().savePreferences(PreferencesDTO())
        val previousEnable = preferencesDatabase.getPreferencesDAO().getEnableWorker()
        preferencesDatabase.getPreferencesDAO().setEnableWorker(true)

        // WHEN new value is set
        val result = preferencesDatabase.getPreferencesDAO().getEnableWorker()

        // then

        MatcherAssert.assertThat(result, CoreMatchers.`is`(true))
        MatcherAssert.assertThat(previousEnable, CoreMatchers.`is`(false))
    }

    @Test
    fun savePreferences_updateEachField() = runBlockingTest {
        // given true enable worker and get the current value of enable worker as false
        val enable = true
        val frequency = 7L
        val hour = 3
        val minute = 3
        val confirm = true
        val preference = PreferencesDTO()
        preferencesDatabase.getPreferencesDAO().savePreferences(preference)

        // WHEN new value is set
        preferencesDatabase.getPreferencesDAO().setEnableWorker(enable)
        preferencesDatabase.getPreferencesDAO().setFrequency(frequency)
        preferencesDatabase.getPreferencesDAO().setSchedule(hour, minute)
        preferencesDatabase.getPreferencesDAO().setConfirmBeforeApply(confirm)
        val result = preferencesDatabase.getPreferencesDAO().getPreferences()

        // then
        MatcherAssert.assertThat(result,CoreMatchers.notNullValue())
        MatcherAssert.assertThat(result?.enable, CoreMatchers.`is`(enable))
        MatcherAssert.assertThat(result?.enable, CoreMatchers.not(preference.enable))
        MatcherAssert.assertThat(result?.frequency, CoreMatchers.`is`(frequency))
        MatcherAssert.assertThat(result?.frequency, CoreMatchers.not(preference.frequency))
        MatcherAssert.assertThat(result?.hour, CoreMatchers.`is`(hour))
        MatcherAssert.assertThat(result?.hour, CoreMatchers.not(preference.hour))
        MatcherAssert.assertThat(result?.minute, CoreMatchers.`is`(minute))
        MatcherAssert.assertThat(result?.minute, CoreMatchers.not(preference.minute))
        MatcherAssert.assertThat(result?.confirmBeforeApply, CoreMatchers.`is`(confirm))
        MatcherAssert.assertThat(
            result?.confirmBeforeApply,
            CoreMatchers.not(preference.confirmBeforeApply)
        )
    }

    @Test
    fun savePreferences_updatePreferences() = runBlockingTest {
        val enable = true
        val frequency = 7L
        val hour = 3
        val minute = 3
        val confirm = true
        val newPreference = PreferencesDTO(enable, frequency, hour, minute, confirm)
        val preference = PreferencesDTO()
        preferencesDatabase.getPreferencesDAO().savePreferences(preference)

        // WHEN new value is set
        preferencesDatabase.getPreferencesDAO().savePreferences(newPreference)
        val result = preferencesDatabase.getPreferencesDAO().getPreferences()

        // then
        MatcherAssert.assertThat(result?.enable, CoreMatchers.`is`(newPreference.enable))
        MatcherAssert.assertThat(result?.enable, CoreMatchers.not(preference.enable))
        MatcherAssert.assertThat(result?.frequency, CoreMatchers.`is`(newPreference.frequency))
        MatcherAssert.assertThat(result?.frequency, CoreMatchers.not(preference.frequency))
        MatcherAssert.assertThat(result?.hour, CoreMatchers.`is`(newPreference.hour))
        MatcherAssert.assertThat(result?.hour, CoreMatchers.not(preference.hour))
        MatcherAssert.assertThat(result?.minute, CoreMatchers.`is`(newPreference.minute))
        MatcherAssert.assertThat(result?.minute, CoreMatchers.not(preference.minute))
        MatcherAssert.assertThat(
            result?.confirmBeforeApply,
            CoreMatchers.`is`(newPreference.confirmBeforeApply)
        )
        MatcherAssert.assertThat(
            result?.confirmBeforeApply,
            CoreMatchers.not(preference.confirmBeforeApply)
        )
    }

    @Test
    fun savePreferences_getEachField() = runBlockingTest {
        val enable = true
        val frequency = 7L
        val hour = 3
        val minute = 3
        val confirm = true
        val newPreference = PreferencesDTO(enable, frequency, hour, minute, confirm)
        preferencesDatabase.getPreferencesDAO().savePreferences(newPreference)

        // WHEN new value is set
        val resultEnable = preferencesDatabase.getPreferencesDAO().getEnableWorker()
        val resultFrequency = preferencesDatabase.getPreferencesDAO().getFrequency()
        val resultHour = preferencesDatabase.getPreferencesDAO().getScheduledHour()
        val resultMinute = preferencesDatabase.getPreferencesDAO().getScheduledMinute()
        val resultConfirm = preferencesDatabase.getPreferencesDAO().getConfirmBeforeApply()

        // then
        MatcherAssert.assertThat(resultEnable, CoreMatchers.`is`(newPreference.enable))
        MatcherAssert.assertThat(resultFrequency, CoreMatchers.`is`(newPreference.frequency))
        MatcherAssert.assertThat(resultHour, CoreMatchers.`is`(newPreference.hour))
        MatcherAssert.assertThat(resultMinute, CoreMatchers.`is`(newPreference.minute))
        MatcherAssert.assertThat(
            resultConfirm,
            CoreMatchers.`is`(newPreference.confirmBeforeApply)
        )
    }
}