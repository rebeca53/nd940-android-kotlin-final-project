package com.rebeca.spacewallpaper.data.local

import android.content.Context
import androidx.room.Room

/**
 * Singleton class that is used to create a favorites db
 */
object LocalDB {

    /**
     * static method that creates a favorite class and returns the DAO of the favorite
     */
    fun createFavoritesDao(context: Context): SpaceImageDAO {
        return Room.databaseBuilder(
            context.applicationContext,
            SpaceImageDatabase::class.java, "favoriteSpaceImages.db"
        ).build().spaceImageDAO()
    }

}