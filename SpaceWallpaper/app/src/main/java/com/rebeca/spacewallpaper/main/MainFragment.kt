package com.rebeca.spacewallpaper.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.rebeca.spacewallpaper.R
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
            val url = viewModel.pictureOfDay.value?.hdurl
            val bundle = bundleOf("url" to url)
            findNavController().navigate(R.id.action_mainFragment_to_favoritesFragment, bundle)
        }

        binding.viewModel = viewModel
        // Inflate the layout for this fragment
        return binding.root
    }
}