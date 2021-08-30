package com.rebeca.spacewallpaper.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.rebeca.spacewallpaper.databinding.FragmentMainBinding
import org.koin.android.ext.android.inject

class MainFragment : Fragment() {
    companion object {
        private const val TAG = "MainFragment"
    }
    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by inject()
    //todo add info icon in the main screen to display data.

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.saveButton.setOnClickListener {
            viewModel.saveSpaceImageToFavorites()
        }

        binding.downloadButton.setOnClickListener {
            viewModel.downloadSpaceImage()
        }

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
}