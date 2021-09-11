package com.rebeca.spacewallpaper.data.local.preferences

import com.rebeca.spacewallpaper.data.PreferencesRepository
import com.rebeca.spacewallpaper.data.PreferencesRepository.Companion.NULL_REFERENCE_MESSAGE
import com.rebeca.spacewallpaper.data.local.RequestResult
import com.rebeca.spacewallpaper.data.local.SpaceImageDAO
import com.rebeca.spacewallpaper.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.security.spec.ECField
import kotlin.math.min

class PreferencesLocalRepository(
    private val preferencesDAO: PreferencesDAO,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): PreferencesRepository {

    override suspend fun savePreferences(preferencesDTO: PreferencesDTO) = withContext(ioDispatcher) {
        wrapEspressoIdlingResource {
            preferencesDAO.savePreferences(preferencesDTO)
        }
    }

    override suspend fun getPreferences(): RequestResult<PreferencesDTO> = withContext(ioDispatcher) {
        wrapEspressoIdlingResource {
            return@withContext try {
                val preferences = preferencesDAO.getPreferences()
                if (preferences == null) {
                    RequestResult.Error(NULL_REFERENCE_MESSAGE)
                } else {
                    RequestResult.Success(preferences)
                }
            } catch (ex: Exception) {
                RequestResult.Error(ex.localizedMessage)
            }
        }
    }
    /**
     * Get if user enables the UpdateWallpaperWorker
     * @return Result that holds a Success with the current enable value
     * or an Error object with the error message
     */
    override suspend fun getEnableWorker(): RequestResult<Boolean> = withContext(ioDispatcher) {
        wrapEspressoIdlingResource {
            return@withContext try {
                val enable = preferencesDAO.getEnableWorker()
                if (enable == null) {
                    RequestResult.Error(NULL_REFERENCE_MESSAGE)
                } else {
                    RequestResult.Success(enable)
                }
            } catch (ex: Exception) {
                RequestResult.Error(ex.localizedMessage)
            }
        }
    }

    /**
     * Set if user enables the UpdateWallpaperWorker to run
     * @param enable true if user enables it, false otherwise
     */
    override suspend fun setEnableWorker(enable: Boolean) = withContext(ioDispatcher) {
        wrapEspressoIdlingResource {
            preferencesDAO.setEnableWorker(enable)
        }
    }

    /**
     * Get how often the worker will fetch new image and set wallpaper
     * @return Result that holds a Success with the frequency or an Error object with the error message
     */
    override suspend fun getFrequency(): RequestResult<Long> = withContext(ioDispatcher) {
        wrapEspressoIdlingResource {
            return@withContext try {
                RequestResult.Success(preferencesDAO.getFrequency())
            } catch (ex: Exception) {
                RequestResult.Error(ex.localizedMessage)
            }
        }
    }

    /**
     * Set how often the worker will fetch a new image and set wallpaper
     * @param frequency a frequency in days. It can be 1 to mean every day, 2 to mean every other day
     * and 7 to mean every week.
     */
    override suspend fun setFrequency(frequency: Long) = withContext(ioDispatcher){
        wrapEspressoIdlingResource {
            preferencesDAO.setFrequency(frequency)
        }
    }

    /**
     * Get the hour in the day that the worker fetches a new image and set wallpaper
     * @return Result that holds a Success with the scheduled hour or an Error object with the error message
     */
    override suspend fun getScheduledHour(): RequestResult<Int> = withContext(ioDispatcher) {
        wrapEspressoIdlingResource {
            return@withContext try {
                RequestResult.Success(preferencesDAO.getScheduledHour())
            } catch (ex: Exception) {
                RequestResult.Error(ex.localizedMessage)
            }
        }
    }

    /**
     * Get the minute in the day that the worker fetches a new image and set wallpaper
     * @return Result that holds a Success with the scheduled minutes or an Error object with the error message
     */
    override suspend fun getScheduledMinute(): RequestResult<Int> = withContext(ioDispatcher) {
        wrapEspressoIdlingResource {
            return@withContext try {
                RequestResult.Success(preferencesDAO.getScheduledMinute())
            } catch (ex: Exception) {
                RequestResult.Error(ex.localizedMessage)
            }
        }
    }

    /**
     * Set the scheduled hour and minute to which the worker fetches a new image and set wallpaper
     * @param hour the scheduled hour as an integer between 0 and 24.
     * @param minute the scheduled minute as an integer between 0 and 59.
     */
    override suspend fun setSchedule(hour: Int, minute: Int) = withContext(ioDispatcher) {
        wrapEspressoIdlingResource {
            var validatedHour = hour
            var validatedMinute = minute

            if (validatedHour > 24 || validatedHour < 0) {
                validatedHour = PreferencesRepository.DEFAULT_HOUR
            }
            if (validatedMinute > 59 || validatedMinute < 0) {
                validatedMinute = PreferencesRepository.DEFAULT_MINUTE
            }

            preferencesDAO.setSchedule(validatedHour, validatedMinute)
        }
    }

    /**
     * Get if user wants to be notified and asked before applying the new wallpaper
     * @return Result that holds a Success with the confirm before apply info or an Error object with the error message
     */
    override suspend fun getConfirmBeforeApply(): RequestResult<Boolean> = withContext(ioDispatcher) {
        wrapEspressoIdlingResource {
            return@withContext try {
                RequestResult.Success(preferencesDAO.getConfirmBeforeApply())
            } catch (ex: Exception) {
                RequestResult.Error(ex.localizedMessage)
            }
        }
    }

    /**
     * Set if the user wants to be notified and asked before applying the new wallpaper
     * @param confirm true if user wants to be asked, false otherwise
     */
    override suspend fun setConfirmBeforeApply(confirm: Boolean) = withContext(ioDispatcher) {
        wrapEspressoIdlingResource {
            preferencesDAO.setConfirmBeforeApply(confirm)
        }
    }
}