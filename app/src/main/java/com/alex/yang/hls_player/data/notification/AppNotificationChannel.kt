package com.alex.yang.hls_player.data.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by AlexYang on 2025/12/23.
 *
 *
 */
@Singleton
class AppNotificationChannel @Inject constructor(
    @ApplicationContext val context: Context
) {
    fun init() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        NotificationManagerCompat.from(context)
            .createNotificationChannel(
                NotificationChannel(
                    CHANNEL_ID_PLAYBACK,
                    DEFAULT_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_LOW
                ).apply {
                    description = "Media playback controls"
                    setShowBadge(false)
                }
            )
    }

    companion object {
        const val CHANNEL_ID_PLAYBACK = "playback"
        const val DEFAULT_CHANNEL_NAME = "Playback"
    }
}