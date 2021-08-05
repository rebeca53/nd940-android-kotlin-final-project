package com.rebeca.spacewallpaper.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.rebeca.spacewallpaper.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}