package com.rebeca.spacewallpaper.data.local.preferences

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * The Room Database that contains the preferences table.
 */
@Database(
    entities = [PreferencesDTO::class], version = 1, exportSchema = false)
abstract class PreferencesDatabase: RoomDatabase() {
    abstract fun getPreferencesDAO(): PreferencesDAO
}