package com.alex.yang.hls_player.data.service

import android.app.Notification
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.ui.PlayerNotificationManager
import com.alex.yang.hls_player.MainActivity
import com.alex.yang.hls_player.data.notification.AppNotificationChannel.Companion.CHANNEL_ID_PLAYBACK
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by AlexYang on 2025/12/23.
 *
 *
 */
@AndroidEntryPoint
class PlaybackService : MediaSessionService() {
    @Inject lateinit var exoPlayer: ExoPlayer

    private var mediaSession: MediaSession? = null
    private var notificationManager: PlayerNotificationManager? = null

    override fun onCreate() {
        super.onCreate()

        mediaSession = MediaSession.Builder(this, exoPlayer).build()

        notificationManager = PlayerNotificationManager.Builder(
            this,
            NOTIFICATION_ID,
            CHANNEL_ID_PLAYBACK
        )
            .setMediaDescriptionAdapter(NotificationAdapter())
            .setNotificationListener(NotificationListener())
            .build()
            .apply {
                setPlayer(exoPlayer)
                mediaSession?.let { setMediaSessionToken(it.platformToken) }
            }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_PLAY -> {
                prepareFromIntent(intent)
                exoPlayer.play()
            }

            ACTION_PAUSE -> exoPlayer.pause()
            ACTION_STOP -> stopAndReset()
        }
        return START_NOT_STICKY
    }

    private fun prepareFromIntent(intent: Intent) {
        val url = intent.getStringExtra(EXTRA_URL) ?: return

        val mediaItem = MediaItem.Builder()
            .setUri(url)
            .setMimeType(MimeTypes.APPLICATION_M3U8)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(intent.getStringExtra(EXTRA_TITLE))
                    .setArtist(intent.getStringExtra(EXTRA_ARTIST))
                    .setArtworkUri(
                        intent.getStringExtra(EXTRA_ARTWORK)?.let(Uri::parse)
                    )
                    .build()
            )
            .build()

        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
    }

    private fun stopAndReset() {
        exoPlayer.stop()
        exoPlayer.clearMediaItems()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }


    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        stopAndReset()

        super.onTaskRemoved(rootIntent)
    }

    override fun onDestroy() {
        notificationManager?.setPlayer(null)
        mediaSession?.release()

        super.onDestroy()
    }

    inner class NotificationAdapter : PlayerNotificationManager.MediaDescriptionAdapter {
        override fun getCurrentContentTitle(player: Player) =
            player.mediaMetadata.title?.toString().orEmpty()

        override fun getCurrentContentText(player: Player) =
            player.mediaMetadata.artist?.toString()

        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ) = null

        override fun createCurrentContentIntent(player: Player): PendingIntent =
            TaskStackBuilder.create(this@PlaybackService).run {
                addNextIntent(Intent(this@PlaybackService, MainActivity::class.java))

                getPendingIntent(
                    0,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            }
    }

    inner class NotificationListener : PlayerNotificationManager.NotificationListener {
        override fun onNotificationPosted(id: Int, notification: Notification, ongoing: Boolean) {
            startForeground(id, notification)
        }

        override fun onNotificationCancelled(id: Int, dismissedByUser: Boolean) {
            stopAndReset()
        }
    }

    companion object {
        const val NOTIFICATION_ID = 1

        const val ACTION_PLAY = "action.PLAY"
        const val ACTION_PAUSE = "action.PAUSE"
        const val ACTION_STOP = "action.STOP"

        const val EXTRA_URL = "extra.URL"
        const val EXTRA_TITLE = "extra.TITLE"
        const val EXTRA_ARTIST = "extra.ARTIST"
        const val EXTRA_ARTWORK = "extra.ARTWORK"
    }
}
