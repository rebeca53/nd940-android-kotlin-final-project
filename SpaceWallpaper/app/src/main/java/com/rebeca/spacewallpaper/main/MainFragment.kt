package com.rebeca.spacewallpaper.main

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.work.*
import com.rebeca.spacewallpaper.databinding.FragmentMainBinding
import com.rebeca.spacewallpaper.R
import com.rebeca.spacewallpaper.work.UpdateWallpaperWorker
import java.util.concurrent.TimeUnit

class MainFragment : Fragment() {

    companion object {
        private const val TAG = "MainFragment"
    }
    private lateinit var binding: FragmentMainBinding

    private lateinit var workManager: WorkManager
    private val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresStorageNotLow(true)
        .setRequiresBatteryNotLow(true)
        .build()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val applicationContext = activity?.applicationContext as Context
        workManager = WorkManager.getInstance(applicationContext)

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_main,
            container,
            false)

        binding.settingsButton.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToSettingsFragment())
        }

        binding.favoritesButton.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToFavoritesFragment())
        }
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecurringWork()
    }

    private fun setupRecurringWork() {
        val wallpaperWorker = PeriodicWorkRequestBuilder<UpdateWallpaperWorker>(
            1,
            TimeUnit.DAYS)
//            .setInitialDelay() //todo set specific time
            .setConstraints(constraints)
            .addTag(UpdateWallpaperWorker.WORK_TAG)
            .build()

       workManager.enqueueUniquePeriodicWork(
            UpdateWallpaperWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
           wallpaperWorker)
    }
}