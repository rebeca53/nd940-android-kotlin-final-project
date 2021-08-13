package com.rebeca.spacewallpaper.data.local

import android.content.Context
import androidx.room.Room

private lateinit var INSTANCE: SpaceImageDAO

/**
 * Singleton class that is used to create a favorites db
 */
object LocalDB {
    /**
     * static method that creates a favorite class and returns the DAO of the favorite
     */
    fun getSpaceImageDatabase(context: Context): SpaceImageDAO {
        synchronized(SpaceImageDAO::class.java) {
            if (!::INSTANCE.isInitialized) {
                INSTANCE =  Room.databaseBuilder(
                    context.applicationContext,
                    SpaceImageDatabase::class.java, "spaceImages.db"
                ).build().spaceImageDAO()
            }
        }
        return INSTANCE
    }

}