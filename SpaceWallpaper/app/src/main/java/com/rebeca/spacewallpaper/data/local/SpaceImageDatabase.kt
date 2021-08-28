package com.rebeca.spacewallpaper.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rebeca.spacewallpaper.data.local.favorites.FavoriteDTO
import com.rebeca.spacewallpaper.data.local.pictureofday.PictureOfDayDTO

/**
 * The Room Database that contains the space image table.
 */
@Database(
    entities = [FavoriteDTO::class, PictureOfDayDTO::class], version = 1, exportSchema = false)
abstract class SpaceImageDatabase : RoomDatabase() {

    abstract fun spaceImageDAO(): SpaceImageDAO
}