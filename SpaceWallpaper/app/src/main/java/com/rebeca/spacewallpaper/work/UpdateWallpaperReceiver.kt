package com.rebeca.spacewallpaper.work

import android.app.NotificationManager
import android.app.WallpaperManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream
import java.lang.Exception
import java.net.URL

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
                        val wallpaperManager = WallpaperManager.getInstance(context)
                        try {
                            val input: InputStream = URL(uri?.toString()).openStream()
                            Log.v(TAG, "${uri?.toString()}")
                            wallpaperManager.setStream(input)
                            input.close()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
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
}