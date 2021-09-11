package com.rebeca.spacewallpaper.data

import com.rebeca.spacewallpaper.data.local.RequestResult
import com.rebeca.spacewallpaper.data.local.preferences.PreferencesDTO

interface PreferencesRepository {

    companion object {
        const val DEFAULT_HOUR = 12
        const val DEFAULT_MINUTE = 0
        const val NULL_REFERENCE_MESSAGE = "Preferences is null"
    }
    suspend fun savePreferences(preferencesDTO: PreferencesDTO)
    suspend fun getPreferences(): RequestResult<PreferencesDTO>
    suspend fun getEnableWorker(): RequestResult<Boolean>
    suspend fun setEnableWorker(enable: Boolean)
    suspend fun getFrequency(): RequestResult<Long>
    suspend fun setFrequency(frequency: Long)
    suspend fun getScheduledHour(): RequestResult<Int>
    suspend fun getScheduledMinute(): RequestResult<Int>
    suspend fun setSchedule(hour: Int, minute: Int)
    suspend fun getConfirmBeforeApply(): RequestResult<Boolean>
    suspend fun setConfirmBeforeApply(confirm: Boolean)
}