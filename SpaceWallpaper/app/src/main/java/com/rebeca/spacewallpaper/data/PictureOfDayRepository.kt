package com.rebeca.spacewallpaper.data

import com.rebeca.spacewallpaper.data.local.Result
import com.rebeca.spacewallpaper.data.local.pictureofday.PictureOfDayDTO

/**
 * Main entry point for accessing picture of day data.
 */
interface PictureOfDayRepository {
    suspend fun refreshPictureOfDay()
    suspend fun getPictureOfDay(): Result<PictureOfDayDTO>
    suspend fun updatePictureOfDay(pictureOfDayDTO: PictureOfDayDTO)
    suspend fun savePictureOfDay(pictureOfDayDTO: PictureOfDayDTO)
    suspend fun deleteAll()
}