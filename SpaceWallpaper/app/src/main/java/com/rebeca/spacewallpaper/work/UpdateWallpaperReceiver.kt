package com.rebeca.spacewallpaper.work

import android.app.NotificationManager
import android.app.WallpaperManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpdateWallpaperReceiver : BroadcastReceiver() {

    companion object {
        const val TAG = "UpdateWallpaperReceiver"
        const val ACTION_CANCEL = "cancel"
        const val ACTION_UPDATE_WALLPAPER = "update"
        const val EXTRA_URI = "uri"
    }

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        when (intent.action) {
            ACTION_CANCEL -> {
                Log.d(TAG, "Cancel wallpaper action")
                cancelNotification(context)
            }
            ACTION_UPDATE_WALLPAPER -> {
                Log.d(TAG, "Update wallpaper action")
                CoroutineScope(Dispatchers.IO).launch {
                    runCatching{
                        val uri = intent.getParcelableExtra<Uri>(EXTRA_URI)
                        var result: Bitmap = Picasso.with(context)
                            .load(uri)
                            .get()
                        result = cropWallpaper(context, result)
                        val wallpaperManager = WallpaperManager.getInstance(context)
                        wallpaperManager.setBitmap(result)
                    }
                }
                cancelNotification(context)
            }
            else -> Log.d(TAG, "Unknown action")
        }
    }

    private fun cancelNotification(context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(UpdateWallpaperWorker.FOREGROUND_NOTIFICATION_ID)
    }

    private fun cropWallpaper(context: Context, bitmap: Bitmap): Bitmap {
        val displayMetrics = context.resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels
        val screenWidth = displayMetrics.widthPixels

        if (bitmap.height <= screenHeight && bitmap.width <= screenWidth) {
            return bitmap
        }

        val startX: Int = (bitmap.width - screenWidth) / 2
        val startY: Int = (bitmap.height - screenHeight) / 2

        return Bitmap.createBitmap(bitmap, startX, startY,
            screenWidth,
            screenHeight)
    }
}