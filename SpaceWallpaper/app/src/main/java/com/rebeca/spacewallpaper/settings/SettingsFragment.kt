package com.rebeca.spacewallpaper.settings

import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.preference.*
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.rebeca.spacewallpaper.BuildConfig
import com.rebeca.spacewallpaper.R
import org.koin.android.ext.android.inject

class SettingsFragment : PreferenceFragmentCompat() {

    private val viewModel: SettingsViewModel by inject()

    private var timePreference: Preference? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val enablePreference = findPreference("enable") as SwitchPreferenceCompat?
        enablePreference?.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
            val enable = newValue as Boolean
            viewModel.setEnable(enable)
            true
        }

        val frequencyPreference = findPreference("frequency") as ListPreference?
        frequencyPreference?.onPreferenceChangeListener =  Preference.OnPreferenceChangeListener { preference, newValue ->
            val frequency = when (newValue as String) {
                "every_day" -> 1L
                "every_two_days" -> 2L
                "every_week" -> 7L
                else -> 1L
            }
            viewModel.setFrequency(frequency)
            true
        }

        timePreference = findPreference("time") as Preference?
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
                viewModel.setScheduledTime(picker.hour, picker.minute)
            }
            true
        }

        val notificationPreference = findPreference("notify") as SwitchPreferenceCompat?
        notificationPreference?.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
            val notify = newValue as Boolean
            viewModel.setConfirmBeforeApply(notify)
            true
        }

        val versionPreference = findPreference("version") as Preference?
        val versionSummaryProvider = Preference.SummaryProvider<Preference> { preference ->
            BuildConfig.VERSION_NAME
        }
        versionPreference?.summaryProvider = versionSummaryProvider
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.currentPreferencesDTO.observe(viewLifecycleOwner, Observer {
            viewModel.updateWork()
            val syncSummaryProvider = Preference.SummaryProvider<Preference> { preference ->
                "Time set to ${viewModel.currentPreferencesDTO.value?.hour}:${viewModel.currentPreferencesDTO.value?.minute}"
            }
            timePreference?.summaryProvider = syncSummaryProvider
        })
    }

}

