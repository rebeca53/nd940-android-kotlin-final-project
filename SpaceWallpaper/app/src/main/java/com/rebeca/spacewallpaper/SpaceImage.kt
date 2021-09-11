package com.rebeca.spacewallpaper

import com.squareup.moshi.Json

data class SpaceImage(@Json(name = "media_type") val mediaType: String, val title: String,
                      val url: String, val hdurl: String, val explanation: String)