package com.rebeca.spacewallpaper.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.rebeca.spacewallpaper.data.PreferencesRepository

class SettingsViewModel( application: Application,
                         private val preferencesRepository: PreferencesRepository
):
    AndroidViewModel(application) {
        companion object {
            private const val TAG = "SettingsViewModel"

        }
}