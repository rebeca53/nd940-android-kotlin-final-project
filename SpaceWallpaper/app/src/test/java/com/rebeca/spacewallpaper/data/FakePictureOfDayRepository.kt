package com.rebeca.spacewallpaper.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.rebeca.spacewallpaper.data.local.RequestResult
import com.rebeca.spacewallpaper.data.local.pictureofday.PictureOfDayDTO
import com.rebeca.spacewallpaper.data.local.pictureofday.PictureOfDayLocalRepository

class FakePictureOfDayRepository : PictureOfDayRepository {
    companion object {
        const val EXCEPTION_MESSAGE = "Test Exception"
    }
    private var shouldReturnError = false
    var currentPictureOfDayDTO: PictureOfDayDTO? = null
    var newPictureOfDayDTO: PictureOfDayDTO? = null
    private val observablePictureOfDay = MutableLiveData<RequestResult<PictureOfDayDTO>>()

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override suspend fun refreshPictureOfDay() {
        if (currentPictureOfDayDTO?.mediaType == newPictureOfDayDTO?.mediaType
            && currentPictureOfDayDTO?.title == newPictureOfDayDTO?.title
            && currentPictureOfDayDTO?.url == newPictureOfDayDTO?.url
            && currentPictureOfDayDTO?.hdurl == newPictureOfDayDTO?.hdurl
            && currentPictureOfDayDTO?.explanation == newPictureOfDayDTO?.explanation
        ) {
            return
        }

        currentPictureOfDayDTO = newPictureOfDayDTO
    }

    override suspend fun getPictureOfDay(): RequestResult<PictureOfDayDTO> {
        if (shouldReturnError) {
            return RequestResult.Error(EXCEPTION_MESSAGE)
        }
        return if (currentPictureOfDayDTO == null)
            RequestResult.Error(PictureOfDayRepository.NOT_FOUND)
        else
            RequestResult.Success(currentPictureOfDayDTO as PictureOfDayDTO)
    }

    override suspend fun updatePictureOfDay(pictureOfDayDTO: PictureOfDayDTO) {
        currentPictureOfDayDTO = pictureOfDayDTO
    }

    override suspend fun savePictureOfDay(pictureOfDayDTO: PictureOfDayDTO) {
        currentPictureOfDayDTO = pictureOfDayDTO
    }

    override suspend fun deleteAll() {
        currentPictureOfDayDTO = null
        newPictureOfDayDTO = null
    }

}