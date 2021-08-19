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
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import com.rebeca.spacewallpaper.R

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
    private var downloadManager: DownloadManager
    private var downloadId: Long = -1
    private val downloadReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when {
                intent == null -> Log.e(TAG, "Intent is null")
                intent.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE -> {
                    val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                    if (id == downloadId) {
                        Toast.makeText(context, R.string.download_completed, Toast.LENGTH_LONG).show()
                    }
                }
                else -> Log.e(TAG, "Unknown broadcast")
            }
        }
    }


    init {
        val context = getApplication<Application>().applicationContext
        downloadManager =
            context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val intentFilter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        intentFilter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED)
        context.registerReceiver(downloadReceiver, intentFilter)
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

        pictureOfDay.value?.let {
            val uri: Uri =
                Uri.parse(it.hdurl)
            val request = DownloadManager.Request(uri)
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, it.title)
            downloadId = downloadManager.enqueue(request)
        }
    }
}