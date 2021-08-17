package com.rebeca.spacewallpaper.work

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.WallpaperManager
import android.content.Context
import android.net.Uri
import com.rebeca.spacewallpaper.R
import com.rebeca.spacewallpaper.data.local.LocalDB
import com.rebeca.spacewallpaper.data.local.RequestResult
import com.rebeca.spacewallpaper.data.local.pictureofday.PictureOfDayLocalRepository
import retrofit2.HttpException
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

class UpdateWallpaperWorkerFactory {
    companion object {
        fun setupWork(workEnabled: Boolean = false,
                      workManager: WorkManager,
                      frequencyInDays: Long = 1,
                      scheduledHour: Long = 12,
                      scheduledMinute: Long = 0,
                      confirmBeforeApply: Boolean = false)
        {
            workManager.cancelAllWorkByTag(UpdateWallpaperWorker.WORK_TAG)

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresStorageNotLow(true)
                .setRequiresBatteryNotLow(true)
                .build()

            val delayWorkTime = calculateDelayInMinutes(scheduledHour, scheduledMinute)
            Log.d("UpdateWallpaper", "delay to worker: $delayWorkTime")

            if (workEnabled) {
                val wallpaperWorker = PeriodicWorkRequestBuilder<UpdateWallpaperWorker>(
                    frequencyInDays,
                    TimeUnit.DAYS
                )
                    .setInputData(workDataOf(UpdateWallpaperWorker.CONFIRMATION_ENABLED to confirmBeforeApply))
                    .setInitialDelay(delayWorkTime, TimeUnit.MINUTES)
                    .setConstraints(constraints)
                    .addTag(UpdateWallpaperWorker.WORK_TAG)
                    .build()

                workManager.enqueueUniquePeriodicWork(
                    UpdateWallpaperWorker.WORK_NAME,
                    ExistingPeriodicWorkPolicy.REPLACE,
                    wallpaperWorker
                )
            }
        }

        private fun calculateDelayInMinutes(scheduledHour: Long, scheduledMinute: Long): Long {
            val calendar: Calendar = Calendar.getInstance()
            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            val currentMinute = calendar.get(Calendar.MINUTE)

            val totalCurrentMinutes = currentMinute + currentHour*60
            val scheduledTotalMinutes = scheduledMinute + scheduledHour*60

            val diffMinutes = scheduledTotalMinutes - totalCurrentMinutes

            return if (diffMinutes > 0)
                diffMinutes
            else
                24*60 - diffMinutes
        }
    }
}

class UpdateWallpaperWorker(appContext: Context, params: WorkerParameters):
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "UpdateWallpaperWorker"
        const val WORK_TAG = "imageWork"
        const val CONFIRMATION_ENABLED = "CONFIRMATION_ENABLED"
        const val FOREGROUND_NOTIFICATION_ID = 123456
        const val CHANNEL_ID = "123456"
    }

    private val wallpaperManager = WallpaperManager.getInstance(applicationContext)
    private val notificationManager =
        applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override suspend fun doWork(): Result {
        val confirmBeforeApply = inputData.getBoolean(CONFIRMATION_ENABLED, false)
        val database = LocalDB.getSpaceImageDatabase(applicationContext)
        val repository = PictureOfDayLocalRepository(database)

        return try {
            repository.deleteAll()
            repository.refreshPictureOfDay()

            val image = repository.getPictureOfDay() as RequestResult.Success
            val uri = Uri.parse(image.data.hdurl)
            // todo download image to internal storage
            // todo update filepath in database

            if (confirmBeforeApply) {
                notifyConfirmChangeWallpaper()
            } else {
                changeWallpaper(uri)
                Log.d(WORK_NAME, "Wallpaper automatically changed with success")
            }
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

    private fun changeWallpaper(uri: Uri) {
        CoroutineScope(Dispatchers.IO).launch {
            runCatching{
                val result: Bitmap = Picasso.with(applicationContext)
                    .load(uri)
                    .get()
                wallpaperManager.setBitmap(result)
            }
        }
    }

    private fun notifyConfirmChangeWallpaper() {
        val id = applicationContext.getString(R.string.notification_channel_id)
        val title = applicationContext.getString(R.string.notification_title)
        // todo implement cancel and confirm action
        val cancel = applicationContext.getString(R.string.notification_cancel)
        val confirm = applicationContext.getString(R.string.notification_confirm)

        createChannel()

        val notification = NotificationCompat.Builder(applicationContext, id)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setTicker(title)
            .build()

        notificationManager.notify(FOREGROUND_NOTIFICATION_ID, notification)
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = applicationContext.getString(R.string.notification_channel_name)
            val descriptionText = applicationContext.getString(R.string.notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager.createNotificationChannel(mChannel)
        }

    }
}