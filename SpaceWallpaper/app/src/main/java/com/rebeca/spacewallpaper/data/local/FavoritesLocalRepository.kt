package com.rebeca.spacewallpaper.data.local

import com.rebeca.spacewallpaper.data.FavoritesRepository
import com.rebeca.spacewallpaper.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Concrete implementation of a data source as a db.
 *
 * @param favoritesDAO the dao that does the Room db operations
 * @param ioDispatcher a coroutine dispatcher to offload the blocking IO tasks
 */
class FavoritesLocalRepository(
    private val favoritesDAO: FavoritesDAO,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : FavoritesRepository {

    /**
     * Get the favorites list from the local db
     * @return Result the holds a Success with all the favorites or an Error object with the error message
     */
    override suspend fun getFavorites(): Result<List<FavoriteDTO>> = withContext(ioDispatcher) {
        wrapEspressoIdlingResource {
            return@withContext try {
                Result.Success(favoritesDAO.getFavorites())
            } catch (ex: Exception) {
                Result.Error(ex.localizedMessage)
            }
        }
    }

    /**
     * Insert a favorite in the db.
     * @param favorite the favorite to be inserted
     */
    override suspend fun saveFavorite(favorite: FavoriteDTO) = withContext(ioDispatcher){
        wrapEspressoIdlingResource {
            favoritesDAO.saveFavorite(favorite)
        }
    }

    /**
     * Update a favorite in the db.
     * @param favorite the favorite to be updated
     */
    override suspend fun updateFavorite(favorite: FavoriteDTO) = withContext(ioDispatcher){
        wrapEspressoIdlingResource {
            favoritesDAO.updateFavorite(favorite)
        }
    }

    /**
     * Get a favorite by its id
     * @param id to be used to get the favorite
     * @return Result the holds a Success object with the Favorite space image
     *         or an Error object with the error message
     */
    override suspend fun getFavorite(id: String): Result<FavoriteDTO> = withContext(ioDispatcher) {
        wrapEspressoIdlingResource {
            try {
                val reminder = favoritesDAO.getFavoriteById(id)
                if (reminder != null) {
                    return@withContext Result.Success(reminder)
                } else {
                    return@withContext Result.Error("Reminder not found!")
                }
            } catch (e: Exception) {
                return@withContext Result.Error(e.localizedMessage)
            }
        }
    }

    /**
     * Deletes all the favorites in the db
     */
    override suspend fun deleteAllFavorites() {
        wrapEspressoIdlingResource {
            withContext(ioDispatcher) {
                favoritesDAO.deleteAllFavorites()
            }
        }
    }
}
