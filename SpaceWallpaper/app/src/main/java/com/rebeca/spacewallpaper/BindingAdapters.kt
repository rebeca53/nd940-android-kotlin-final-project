package com.rebeca.spacewallpaper

import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.rebeca.spacewallpaper.main.MainViewModel
import com.squareup.picasso.Picasso

@BindingAdapter("pictureOfDay")
fun bindImageViewToPictureOfDay(imageView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Picasso.with(imageView.context)
            .load(imgUri)
            .placeholder(R.drawable.loading_animation)
            .error(R.drawable.ic_broken_image)
            .into(imageView)
    }
}

@BindingAdapter("pictureOfDayContentDescription")
fun bindContentDescriptionToPictureOfDay(imageView: ImageView, title: String?) {
    title?.let {
        val context = imageView.context
        imageView.contentDescription = String.format(context.getString(R.string.nasa_picture_of_day_content_description_format), title)
    }
}

@BindingAdapter("imageOfDayNASAApiStatus")
fun bindImageOfDayStatus(statusImageView: ImageView, status: MainViewModel.NASAApiStatus?) {
    when (status) {
        MainViewModel.NASAApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        MainViewModel.NASAApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_broken_image)
        }
        MainViewModel.NASAApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}