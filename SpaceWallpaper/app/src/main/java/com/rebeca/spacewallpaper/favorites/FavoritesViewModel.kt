package com.rebeca.spacewallpaper.favorites

import android.app.Application
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rebeca.spacewallpaper.R
import com.rebeca.spacewallpaper.SpaceImage
import com.rebeca.spacewallpaper.data.FavoritesRepository
import com.rebeca.spacewallpaper.data.local.RequestResult
import com.rebeca.spacewallpaper.data.local.favorites.FavoriteDTO
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application,
                         private val favoritesRepository: FavoritesRepository):
    AndroidViewModel(application){
    companion object {
        private const val TAG = "FavoritesViewModel"
    }

    val favoritesList = MutableLiveData<List<SpaceImage>>()
    val selectedImage = MutableLiveData<String>()

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
    }

    /**
     * Get all the space images from the FavoritesRepository and add them to the favoritesList to be shown on the UI,
     * or show error if any
     */
    fun loadFavorites() {
        viewModelScope.launch {
            //interacting with the dataSource has to be through a coroutine
            val result = favoritesRepository.getFavorites()
            when (result) {
                is RequestResult.Success<*> -> {
                    val dataList = ArrayList<SpaceImage>()
                    dataList.addAll((result.data as List<FavoriteDTO>).map { favorite ->
                        //map the favorite data from the DB to the be ready to be displayed on the UI
                        SpaceImage(
                            favorite.mediaType,
                            favorite.title,
                            favorite.url,
                            favorite.hdurl,
                            favorite.explanation
                        )
                    })
                    favoritesList.value = dataList
                }
                is RequestResult.Error ->
                    Toast.makeText(getApplication(), result.message, Toast.LENGTH_LONG).show()
            }
        }
    }


    fun downloadImage(spaceImage: SpaceImage) {
        val uri: Uri =
            Uri.parse(spaceImage.hdurl)
        val request = DownloadManager.Request(uri)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, spaceImage.title)
        downloadId = downloadManager.enqueue(request)
    }

}