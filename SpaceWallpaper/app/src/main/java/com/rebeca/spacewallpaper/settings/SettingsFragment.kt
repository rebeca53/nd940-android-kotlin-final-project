package com.rebeca.spacewallpaper.settings

import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.util.Log
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.rebeca.spacewallpaper.R

class SettingsFragment : PreferenceFragmentCompat() {

    private var timePreference: Preference? = null
    private var scheduledHour = 0
    private var scheduledMinute = 0

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        timePreference = findPreference("time")
        val syncSummaryProvider = Preference.SummaryProvider<Preference> { preference ->
            "Time set to $scheduledHour:$scheduledMinute"
        }
        timePreference?.summaryProvider = syncSummaryProvider

        timePreference?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val isSystem24Hour = is24HourFormat(context)
            val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

            val picker = MaterialTimePicker.Builder()
                .setTimeFormat(clockFormat)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Set time")
                .build()
            picker.show(childFragmentManager, "TAG")

            picker.addOnPositiveButtonClickListener {
                Log.d("SettingsFragment", "POSITIVE")
                scheduledHour = picker.hour
                scheduledMinute = picker.minute
                timePreference?.summaryProvider = syncSummaryProvider
            }
            true
        }
    }

}

