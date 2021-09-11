package com.rebeca.spacewallpaper.data.local

import android.content.Context
import androidx.room.Room
import com.rebeca.spacewallpaper.data.local.preferences.PreferencesDAO
import com.rebeca.spacewallpaper.data.local.preferences.PreferencesDatabase

private lateinit var SPACE_IMAGE_INSTANCE: SpaceImageDAO
private lateinit var PREFERENCES_INSTANCE: PreferencesDAO

/**
 * Singleton class that is used to create a favorites db
 */
object LocalDB {
    /**
     * static method that creates a favorite class and returns the DAO of the favorite
     */
    fun getSpaceImageDatabase(context: Context): SpaceImageDAO {
        synchronized(SpaceImageDAO::class.java) {
            if (!::SPACE_IMAGE_INSTANCE.isInitialized) {
                SPACE_IMAGE_INSTANCE =  Room.databaseBuilder(
                    context.applicationContext,
                    SpaceImageDatabase::class.java, "spaceImages.db"
                ).build().spaceImageDAO()
            }
        }
        return SPACE_IMAGE_INSTANCE
    }

    /**
     * static method that creates a favorite class and returns the DAO of the favorite
     */
    fun getPreferencesDatabase(context: Context): PreferencesDAO {
        synchronized(PreferencesDAO::class.java) {
            if (!::PREFERENCES_INSTANCE.isInitialized) {
                PREFERENCES_INSTANCE =  Room.databaseBuilder(
                    context.applicationContext,
                    PreferencesDatabase::class.java, "preferences.db"
                ).build().getPreferencesDAO()
            }
        }
        return PREFERENCES_INSTANCE
    }
}