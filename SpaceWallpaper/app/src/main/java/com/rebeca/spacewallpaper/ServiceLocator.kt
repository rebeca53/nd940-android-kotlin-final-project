package com.rebeca.spacewallpaper

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.rebeca.spacewallpaper.data.FavoritesRepository
import com.rebeca.spacewallpaper.data.PictureOfDayRepository
import com.rebeca.spacewallpaper.data.local.LocalDB
import com.rebeca.spacewallpaper.data.local.SpaceImageDAO
import com.rebeca.spacewallpaper.data.local.favorites.FavoritesLocalRepository
import com.rebeca.spacewallpaper.data.local.pictureofday.PictureOfDayLocalRepository

object ServiceLocator {
    private var spaceImageDao: SpaceImageDAO? = null

    @Volatile
    var favoritesRepository: FavoritesRepository? = null
    @VisibleForTesting set

    @Volatile
    var pictureOfDayRepository: PictureOfDayRepository? = null
    @VisibleForTesting set

    private val lock =Any()

    fun provideFavoritesRepository(context: Context): FavoritesRepository {
        synchronized(this) {
            return favoritesRepository ?: createFavoritesRepository(context)
        }
    }

    private fun createFavoritesRepository(context: Context): FavoritesLocalRepository {
        val newRepo = FavoritesLocalRepository(createLocalSpaceImageDAO(context))
        favoritesRepository = newRepo
        return newRepo
    }

    fun providePictureOfDayRepository(context: Context): PictureOfDayRepository {
        synchronized(this) {
            return pictureOfDayRepository?: createPictureOfDayRepository(context)
        }
    }

    private fun createPictureOfDayRepository(context: Context): PictureOfDayLocalRepository {
        val newRepo = PictureOfDayLocalRepository(createLocalSpaceImageDAO(context))
        pictureOfDayRepository = newRepo
        return newRepo
    }

    private fun createLocalSpaceImageDAO(context: Context): SpaceImageDAO {
        return this.spaceImageDao ?: LocalDB.getSpaceImageDatabase(context)
    }


    @VisibleForTesting
    fun resetRepository() {
        synchronized(lock) {
            spaceImageDao = null
            favoritesRepository = null
        }
    }
}