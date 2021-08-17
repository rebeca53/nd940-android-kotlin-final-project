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
import com.rebeca.spacewallpaper.work.UpdateWallpaperWorkerFactory
import java.util.concurrent.TimeUnit

class MainFragment : Fragment() {

    companion object {
        private const val TAG = "MainFragment"
    }
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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

        //todo use settings to launch service. properly get preferences.
        val applicationContext = activity?.applicationContext as Context
        val workManager = WorkManager.getInstance(applicationContext)
        UpdateWallpaperWorkerFactory.setupWork(true, workManager = workManager)
    }
}