package com.rebeca.spacewallpaper.favorites

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rebeca.spacewallpaper.databinding.FragmentFavoritesBinding
import org.koin.android.ext.android.inject

class FavoritesFragment : Fragment() {
    companion object {
        private const val TAG = "FavoritesFragment"
    }
    private lateinit var binding: FragmentFavoritesBinding
    private val viewModel: FavoritesViewModel by inject()
    private var urlOfDay: String? = null
    //todo receive image of the day by bundle.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel
        viewModel.loadFavorites()

        // Inflate the layout for this fragment
        urlOfDay = arguments?.getString("url")
        Log.d(TAG, "url od the day is $urlOfDay")
        viewModel.selectedImage.value = urlOfDay

        setupRecyclerView()
        return binding.root
    }

    private fun setupRecyclerView() {
        val adapter = FavoritesListAdapter {
            viewModel.selectedImage.value = it.hdurl
        }
        adapter.setDownloadCallback {
            viewModel.downloadImage(it)
        }

//        setup the recycler view using the extension function
        binding.spaceImageRecycler.setup(adapter)
    }
}