package com.rebeca.spacewallpaper.settings

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.rebeca.spacewallpaper.data.PreferencesRepository
import com.rebeca.spacewallpaper.data.local.RequestResult
import com.rebeca.spacewallpaper.data.local.preferences.PreferencesDTO
import com.rebeca.spacewallpaper.work.UpdateWallpaperWorkerFactory
import kotlinx.coroutines.launch
import java.lang.Exception

class SettingsViewModel( application: Application,
                         private val preferencesRepository: PreferencesRepository
):
    AndroidViewModel(application) {
    companion object {
        private const val TAG = "SettingsViewModel"
    }
    val currentPreferencesDTO = MutableLiveData<PreferencesDTO>()

    init {
        getCurrentPreferences()
    }

    private fun getCurrentPreferences() {
        viewModelScope.launch {
            try {
                val result = preferencesRepository.getPreferences()
                if (result is RequestResult.Success) {
                    currentPreferencesDTO.value = result.data
                } else if (result is RequestResult.Error) {
                    preferencesRepository.savePreferences(PreferencesDTO())
                    Log.d(TAG, "Preferences initialized to default.")
                    val doubleCheck = preferencesRepository.getPreferences()
                    if (doubleCheck is RequestResult.Success) {
                        currentPreferencesDTO.value = doubleCheck.data
                    } else if (doubleCheck is RequestResult.Error) {
                        Log.e(TAG, "savePreferences failed ${doubleCheck.message}")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "getCurrentPreferences Failure: ${e.message}")
            }
        }
    }

    fun setEnable(enable: Boolean) {
        viewModelScope.launch {
            try {
                preferencesRepository.setEnableWorker(enable)
            } catch (e: Exception) {
                Log.e(TAG, "setEnable Failure: ${e.message}")
            }
            getCurrentPreferences()
        }
    }

    fun setFrequency(frequency: Long) {
        viewModelScope.launch {
            try {
                preferencesRepository.setFrequency(frequency)
            } catch (e: Exception) {
                Log.e(TAG, "setFrequency Failure: ${e.message}")
            }
            getCurrentPreferences()
        }
    }

    fun setScheduledTime(hour: Int, minute: Int) {
        viewModelScope.launch {
            try {
                preferencesRepository.setSchedule(hour, minute)
            } catch (e: Exception) {
                Log.e(TAG, "setScheduledTime Failure: ${e.message}")
            }
            getCurrentPreferences()
        }
    }

    fun setConfirmBeforeApply(confirm: Boolean) {
        viewModelScope.launch {
            try {
                preferencesRepository.setConfirmBeforeApply(confirm)
            } catch (e: Exception) {
                Log.e(TAG, "setConfirmBeforeApply Failure: ${e.message}")
            }
            getCurrentPreferences()
        }
    }

    fun updateWork() {
        val applicationContext = getApplication<Application>().applicationContext
        val workManager = WorkManager.getInstance(applicationContext)

        if (currentPreferencesDTO.value != null) {
            val preferences = currentPreferencesDTO.value as PreferencesDTO
            UpdateWallpaperWorkerFactory.setupWork(
                preferences.enable,
                workManager,
                preferences.frequency,
                preferences.hour.toLong(),
                preferences.minute.toLong(),
                preferences.confirmBeforeApply
            )
        } else {
            Toast.makeText(applicationContext, "Failed to update settings", Toast.LENGTH_LONG)
                .show()
        }
    }
}