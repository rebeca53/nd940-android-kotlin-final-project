package com.rebeca.spacewallpaper.data.local.pictureofday

import com.rebeca.spacewallpaper.data.PictureOfDayRepository
import com.rebeca.spacewallpaper.data.local.Result
import com.rebeca.spacewallpaper.data.local.SpaceImageDAO
import com.rebeca.spacewallpaper.data.remote.NASAApi
import com.rebeca.spacewallpaper.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class PictureOfDayLocalRepository(
    private val spaceImageDAO: SpaceImageDAO,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): PictureOfDayRepository {
    //todo add info icon in the main screen to display data.
    override suspend fun refreshPictureOfDay() = withContext(ioDispatcher){
        // clear data
        spaceImageDAO.deletePictureOfDay()

        // get new data
        val pictureOfDayResult = NASAApi.retrofitService.getImageOfDay()
        val pictureOfDayDTO = PictureOfDayDTO(
            pictureOfDayResult.mediaType,
            pictureOfDayResult.title,
            pictureOfDayResult.url,
            pictureOfDayResult.hdurl,
            pictureOfDayResult.explanation,
            "")
        spaceImageDAO.savePictureOfDay(pictureOfDayDTO)
    }

    /**
     * Refresh info from NASAApiService. Then retrieve data from the local db.
     */
    override suspend fun getPictureOfDay(): Result<PictureOfDayDTO> = withContext(ioDispatcher) {
        wrapEspressoIdlingResource {
            return@withContext try {
                val picture = spaceImageDAO.getPictureOfDay()
                if (picture != null ) {
                    Result.Success(picture)
                } else {
                    Result.Error("Picture Of Day not found!")
                }
            } catch (ex: Exception) {
                Result.Error(ex.localizedMessage)
            }
        }
    }

    override suspend fun updatePictureOfDay(pictureOfDayDTO: PictureOfDayDTO) = withContext(ioDispatcher) {
        wrapEspressoIdlingResource {
            spaceImageDAO.updatePictureOfDay(pictureOfDayDTO)
        }
    }

    /**
     * Delete any picture of day from db. Insert a new picture of day in the db.
     * @param pictureOfDayDTO the picture of day to be inserted.
     */
    override suspend fun savePictureOfDay(pictureOfDayDTO: PictureOfDayDTO) = withContext(ioDispatcher){
        wrapEspressoIdlingResource {
            spaceImageDAO.deletePictureOfDay()
            spaceImageDAO.savePictureOfDay(pictureOfDayDTO)
        }
    }

    /**
     * Delete picture of day in the db.
     */
    override suspend fun deleteAll() {
        wrapEspressoIdlingResource {
            withContext(ioDispatcher) {
                spaceImageDAO.deletePictureOfDay()
            }
        }
    }
}