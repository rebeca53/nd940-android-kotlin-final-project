package com.rebeca.spacewallpaper.data.local.preferences

import androidx.room.*
import com.rebeca.spacewallpaper.data.local.RequestResult

/**
 * Data Access Object for the preferences database
 */
@Dao
interface PreferencesDAO {
    @Query("SELECT * FROM preferences")
    suspend fun getPreferences(): PreferencesDTO?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePreferences(preferencesDTO: PreferencesDTO)

    @Query("SELECT enable FROM preferences LIMIT 1")
    suspend fun getEnableWorker(): Boolean?
    @Query("UPDATE preferences SET enable = :enable")
    suspend fun setEnableWorker(enable: Boolean)

    @Query("SELECT frequency FROM preferences")
    suspend fun getFrequency(): Long
    @Query("UPDATE preferences SET frequency = :frequency")
    suspend fun setFrequency(frequency: Long)

    @Query("SELECT hour FROM preferences")
    suspend fun getScheduledHour(): Int
    @Query("SELECT minute FROM preferences")
    suspend fun getScheduledMinute(): Int
    @Query("UPDATE preferences SET hour = :hour, minute = :minute")
    suspend fun setSchedule(hour: Int, minute: Int)

    @Query("SELECT confirmBeforeApply FROM preferences")
    suspend fun getConfirmBeforeApply(): Boolean
    @Query("UPDATE preferences SET confirmBeforeApply = :confirm")
    suspend fun setConfirmBeforeApply(confirm: Boolean)
}