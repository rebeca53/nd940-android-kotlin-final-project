package com.rebeca.spacewallpaper.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.rebeca.spacewallpaper.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    companion object {
        private const val TAG = "MainFragment"
    }
    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        ViewModelProvider(this, MainViewModel.Factory(activity.application)).get(MainViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.settingsButton.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToSettingsFragment())
        }

        binding.favoritesButton.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToFavoritesFragment())
        }

        binding.viewModel = viewModel
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //todo use settings to launch service. properly get preferences.
//        val applicationContext = activity?.applicationContext as Context
//        val workManager = WorkManager.getInstance(applicationContext)
//        UpdateWallpaperWorkerFactory.setupWork(true, workManager, 1, 15, 30)
    }
}