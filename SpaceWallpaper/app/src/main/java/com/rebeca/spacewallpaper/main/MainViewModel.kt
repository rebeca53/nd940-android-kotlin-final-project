package com.rebeca.spacewallpaper.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.rebeca.spacewallpaper.SpaceImage
import com.rebeca.spacewallpaper.data.remote.NASAApi
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {
    companion object {
        private const val TAG = "MainViewModel"
    }
    enum class NASAApiStatus {LOADING, ERROR, DONE}

    val statusPictureOfDay = MutableLiveData<NASAApiStatus>()
    val pictureOfDay = MutableLiveData<SpaceImage>()

    init {
        getPictureOfDay()
    }

    private fun getPictureOfDay() {
        statusPictureOfDay.value = NASAApiStatus.LOADING
        viewModelScope.launch {
            try {
                pictureOfDay.value = NASAApi.retrofitService.getImageOfDay()
                Log.d(TAG, "getPictureOfDay returns ${pictureOfDay.value}")
                statusPictureOfDay.value = NASAApiStatus.DONE
            } catch (e: Exception) {
                statusPictureOfDay.value = NASAApiStatus.ERROR
                Log.e(TAG, "getPictureOfDay Failure: ${e.message}")
            }
        }
    }

    /**
     * Factory for constructing MainViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}