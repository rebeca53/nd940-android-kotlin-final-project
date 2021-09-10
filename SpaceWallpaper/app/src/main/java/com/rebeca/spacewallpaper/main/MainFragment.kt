package com.rebeca.spacewallpaper.main

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
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
            scaleButton(binding.saveButton)
        }

        binding.downloadButton.setOnClickListener {
            viewModel.downloadSpaceImage()
            scaleButton(binding.downloadButton)
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

    private fun scaleButton(button: ImageButton) {
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 2f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 2f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(
            button, scaleX, scaleY)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.start()
    }

}