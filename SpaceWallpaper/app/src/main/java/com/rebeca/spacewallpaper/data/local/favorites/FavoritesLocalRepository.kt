package com.rebeca.spacewallpaper.data.local.favorites

import android.util.Log
import com.rebeca.spacewallpaper.data.FavoritesRepository
import com.rebeca.spacewallpaper.data.local.SpaceImageDAO
import com.rebeca.spacewallpaper.data.local.RequestResult
import com.rebeca.spacewallpaper.data.local.pictureofday.PictureOfDayLocalRepository
import com.rebeca.spacewallpaper.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Concrete implementation of a data source as a db.
 *
 * @param spaceImageDAO the dao that does the Room db operations
 * @param ioDispatcher a coroutine dispatcher to offload the blocking IO tasks
 */
class FavoritesLocalRepository(
    private val spaceImageDAO: SpaceImageDAO,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : FavoritesRepository {

    /**
     * Get the favorites list from the local db
     * @return Result the holds a Success with all the favorites or an Error object with the error message
     */
    override suspend fun getFavorites(): RequestResult<List<FavoriteDTO>> = withContext(ioDispatcher) {
        wrapEspressoIdlingResource {
            return@withContext try {
                RequestResult.Success(spaceImageDAO.getFavorites())
            } catch (ex: Exception) {
                RequestResult.Error(ex.localizedMessage)
            }
        }
    }

    /**
     * Insert a favorite in the db.
     * @param favorite the favorite to be inserted
     */
    override suspend fun saveFavorite(favorite: FavoriteDTO) = withContext(ioDispatcher){
        wrapEspressoIdlingResource {
            // get new data
            val currentFavorites = spaceImageDAO.getFavorites()
            for (saved in currentFavorites) {
                // validate if it is new data
                if (favorite.mediaType == saved.mediaType
                    && favorite.title == saved.title
                    && favorite.url == saved.url
                    && favorite.hdurl == saved.hdurl
                    && favorite.explanation == saved.explanation) {
                    Log.d(PictureOfDayLocalRepository.TAG, FavoritesRepository.ALREADY_IN_DATABASE)
                    return@withContext
                }
            }

            spaceImageDAO.saveFavorite(favorite)
        }
    }

    /**
     * Update a favorite in the db.
     * @param favorite the favorite to be updated
     */
    override suspend fun updateFavorite(favorite: FavoriteDTO) = withContext(ioDispatcher){
        wrapEspressoIdlingResource {
            spaceImageDAO.updateFavorite(favorite)
        }
    }

    /**
     * Get a favorite by its id
     * @param id to be used to get the favorite
     * @return Result the holds a Success object with the Favorite space image
     *         or an Error object with the error message
     */
    override suspend fun getFavorite(id: String): RequestResult<FavoriteDTO> = withContext(ioDispatcher) {
        wrapEspressoIdlingResource {
            try {
                val reminder = spaceImageDAO.getFavoriteById(id)
                if (reminder != null) {
                    return@withContext RequestResult.Success(reminder)
                } else {
                    return@withContext RequestResult.Error(FavoritesRepository.NOT_FOUND)
                }
            } catch (e: Exception) {
                return@withContext RequestResult.Error(e.localizedMessage)
            }
        }
    }

    /**
     * Deletes all the favorites in the db
     */
    override suspend fun deleteAllFavorites() {
        wrapEspressoIdlingResource {
            withContext(ioDispatcher) {
                spaceImageDAO.deleteAllFavorites()
            }
        }
    }
}
