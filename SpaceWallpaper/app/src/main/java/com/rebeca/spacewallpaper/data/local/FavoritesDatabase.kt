package com.rebeca.spacewallpaper.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * The Room Database that contains the reminders table.
 */
@Database(entities = [FavoriteDTO::class], version = 1, exportSchema = false)
abstract class FavoritesDatabase : RoomDatabase() {

    abstract fun favoriteDao(): FavoritesDAO
}