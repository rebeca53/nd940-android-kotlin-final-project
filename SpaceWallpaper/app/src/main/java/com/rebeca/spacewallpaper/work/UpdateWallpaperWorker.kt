package com.rebeca.spacewallpaper.work

import android.app.WallpaperManager
import android.content.Context
import android.net.Uri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.rebeca.spacewallpaper.data.local.LocalDB
import com.rebeca.spacewallpaper.data.local.RequestResult
import com.rebeca.spacewallpaper.data.local.pictureofday.PictureOfDayLocalRepository
import retrofit2.HttpException
import android.graphics.Bitmap
import android.util.Log
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class UpdateWallpaperWorker(appContext: Context, params: WorkerParameters):
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "UpdateWallpaperWorker"
        const val WORK_TAG = "imageWork"
    }

    private val wallpaperManager = WallpaperManager.getInstance(applicationContext)

    override suspend fun doWork(): Result {
        val database = LocalDB.getSpaceImageDatabase(applicationContext)
        val repository = PictureOfDayLocalRepository(database)

        return try {
            repository.deleteAll()
            repository.refreshPictureOfDay()

            val image = repository.getPictureOfDay() as RequestResult.Success
            val uri = Uri.parse(image.data.hdurl)
            // todo download image to internal storage
            // todo update filepath in database
            CoroutineScope(Dispatchers.IO).launch {
                runCatching{
                    val result: Bitmap = Picasso.with(applicationContext)
                        .load(uri)
                        .get()

                    wallpaperManager.setBitmap(result)
                }
            }
            // todo notify before set wallpaper
            Log.d(WORK_NAME, "Wallpaper changed with success")
            Result.success()
        }
        catch (e: HttpException) {
            Result.retry()
        }
        catch (ex: IOException) {
            ex.printStackTrace()
            Result.failure()
        }
        catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }
}