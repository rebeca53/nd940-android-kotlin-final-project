package com.rebeca.spacewallpaper.data.local.pictureofday

import android.util.Log
import com.rebeca.spacewallpaper.data.PictureOfDayRepository
import com.rebeca.spacewallpaper.data.local.RequestResult
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
    companion object {
        const val TAG = "PictureOfDayRepository"
    }

    override suspend fun refreshPictureOfDay() = withContext(ioDispatcher){
        // get new data
        val pictureOfDayResult = NASAApi.retrofitService.getImageOfDay()
        val currentPictureOfDayRepo = spaceImageDAO.getPictureOfDay()

        // validate if it is new data
        if (pictureOfDayResult.mediaType == currentPictureOfDayRepo?.mediaType
            && pictureOfDayResult.title == currentPictureOfDayRepo.title
            && pictureOfDayResult.url == currentPictureOfDayRepo.url
            && pictureOfDayResult.hdurl == currentPictureOfDayRepo.hdurl
            && pictureOfDayResult.explanation == currentPictureOfDayRepo.explanation
        ) {
            Log.d(TAG, "Picture of Day is up to date!")
            return@withContext
        }

        // add new data
        val pictureOfDayDTO = PictureOfDayDTO(
            pictureOfDayResult.mediaType,
            pictureOfDayResult.title,
            pictureOfDayResult.url,
            pictureOfDayResult.hdurl,
            pictureOfDayResult.explanation)

        spaceImageDAO.savePictureOfDay(pictureOfDayDTO)
    }

    /**
     * Refresh info from NASAApiService. Then retrieve data from the local db.
     */
    override suspend fun getPictureOfDay(): RequestResult<PictureOfDayDTO> = withContext(ioDispatcher) {
        wrapEspressoIdlingResource {
            return@withContext try {
                val picture = spaceImageDAO.getPictureOfDay()
                if (picture != null ) {
                    RequestResult.Success(picture)
                } else {
                    RequestResult.Error("Picture Of Day not found!")
                }
            } catch (ex: Exception) {
                RequestResult.Error(ex.localizedMessage)
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
            // clear data
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