package com.rebeca.spacewallpaper.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.rebeca.spacewallpaper.data.FavoritesRepository
import com.rebeca.spacewallpaper.data.PictureOfDayRepository
import com.rebeca.spacewallpaper.data.local.RequestResult
import com.rebeca.spacewallpaper.data.local.favorites.FavoriteDTO
import com.rebeca.spacewallpaper.data.local.pictureofday.PictureOfDayDTO
import kotlinx.coroutines.launch
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment

class MainViewModel(application: Application,
                    private val repository: FavoritesRepository,
                    private val pictureOfDayRepository: PictureOfDayRepository):
    AndroidViewModel(application) {
    companion object {
        private const val TAG = "MainViewModel"
    }

    enum class NASAApiStatus {LOADING, ERROR, DONE}

    val statusPictureOfDay = MutableLiveData<NASAApiStatus>()
    val pictureOfDay = MutableLiveData<PictureOfDayDTO>()
    init {
        getPictureOfDay()
    }

    private fun getPictureOfDay() {
        statusPictureOfDay.value = NASAApiStatus.LOADING
        viewModelScope.launch {
            try {
                pictureOfDayRepository.refreshPictureOfDay()
                val result = pictureOfDayRepository.getPictureOfDay() as RequestResult.Success
                pictureOfDay.value = result.data
                Log.d(TAG, "getPictureOfDay returns ${pictureOfDay.value}")
                statusPictureOfDay.value = NASAApiStatus.DONE
            } catch (e: Exception) {
                statusPictureOfDay.value = NASAApiStatus.ERROR
                Log.e(TAG, "getPictureOfDay Failure: ${e.message}")
            }
        }
    }

    fun saveSpaceImageToFavorites() {
        // validate
        if (statusPictureOfDay.value != NASAApiStatus.DONE) {
            return
        }
        if (pictureOfDay.value == null) {
            return
        }
        val favorite = pictureOfDay.value as PictureOfDayDTO

        viewModelScope.launch {
            repository.saveFavorite(
                FavoriteDTO(
                    favorite.mediaType,
                    favorite.title,
                    favorite.url,
                    favorite.hdurl,
                    favorite.explanation)
            )
            Log.d(TAG, "favorite saved! ${favorite.title}")
            val favoritesList = repository.getFavorites() as RequestResult.Success
            for (saved in favoritesList.data) {
                Log.d(TAG, "favorite title ${saved.title}, id ${saved.id}")
            }
        }
    }

    fun downloadSpaceImage() {
        // validate
        if (statusPictureOfDay.value != NASAApiStatus.DONE) {
            return
        }
        if (pictureOfDay.value == null) {
            return
        }

        val context = getApplication<Application>().applicationContext
        val downloadManager =
            context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        pictureOfDay.value?.let {
            val uri: Uri =
                Uri.parse(it.hdurl)
            val request = DownloadManager.Request(uri)
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, it.title)
            val reference: Long = downloadManager.enqueue(request)
        }

        //todo toast when it is done
        // test different days

    }
}