package com.alex.yang.hls_player.player.presentation

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.alex.yang.hls_player.data.model.PlaybackNotificationData
import com.alex.yang.hls_player.data.service.PlaybackService
import com.alex.yang.hls_player.data.service.PlaybackService.Companion.ACTION_PAUSE
import com.alex.yang.hls_player.data.service.PlaybackService.Companion.ACTION_PLAY
import com.alex.yang.hls_player.data.service.PlaybackService.Companion.ACTION_STOP
import com.alex.yang.hls_player.data.service.PlaybackService.Companion.EXTRA_ARTIST
import com.alex.yang.hls_player.data.service.PlaybackService.Companion.EXTRA_ARTWORK
import com.alex.yang.hls_player.data.service.PlaybackService.Companion.EXTRA_TITLE
import com.alex.yang.hls_player.data.service.PlaybackService.Companion.EXTRA_URL
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by AlexYang on 2025/11/28.
 *
 *
 */

@HiltViewModel
class PlayerViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val exoPlayer: ExoPlayer
) : ViewModel() {
    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState = _uiState.asStateFlow()

    val player: ExoPlayer
        get() = exoPlayer

    val fakeNotificationData = PlaybackNotificationData()

    private var hasInitialized = false
    private var progressJob: Job? = null
    private var hasPrepared = false

    private val playerListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            _uiState.update { it.copy(isPlaying = isPlaying) }
        }

        override fun onPlaybackStateChanged(state: Int) {
            if (state == Player.STATE_READY) {
                val mills = exoPlayer.duration
                if (mills > 0) {
                    _uiState.update { it.copy(totalMills = mills) }
                }
            }
        }
    }

    fun setUpPlayer() {
        if (hasInitialized) return

        hasInitialized = true

        exoPlayer.addListener(playerListener)
        startProgressTracker()

        // 同步一次 UI（避免剛進來畫面顯示 0/0）
        _uiState.update {
            it.copy(
                isPlaying = exoPlayer.isPlaying,
                currentMills = exoPlayer.currentPosition,
                totalMills = exoPlayer.duration.takeIf { d -> d > 0 } ?: it.totalMills
            )
        }
    }

    fun play() {
        hasPrepared = true
        sendAction(
            action = ACTION_PLAY,
            data = fakeNotificationData
        )
    }

    fun pause() = sendAction(action = ACTION_PAUSE, data = fakeNotificationData)

    fun stop() = {
        hasPrepared = false
        sendAction(action = ACTION_STOP, data = fakeNotificationData)

        _uiState.update { PlayerUiState() }
    }

    fun togglePlayPause() {
        if (exoPlayer.isPlaying) {
            pause()
            return
        }

        if (!hasPrepared) {
            hasPrepared = true
            sendAction(action = ACTION_PLAY, data = fakeNotificationData,)
        } else {
            sendAction(ACTION_PLAY)
        }
    }

    fun seekTo(ms: Long) {
        exoPlayer.seekTo(ms.coerceAtLeast(0L))
        _uiState.update { it.copy(currentMills = exoPlayer.currentPosition) }
    }

    fun skipPrev1s() = skipBy(-1_000L)

    fun skipPrev10s() = skipBy(-10_000L)

    fun skipNext1s() = skipBy(1_000L)

    fun skipNext10s() = skipBy(10_000L)

    /**
     * 共用的跳秒邏輯，offset 可正可負
     */
    private fun skipBy(offsetMs: Long) {
        val totalMills = exoPlayer.duration.takeIf { it > 0 } ?: Long.MAX_VALUE
        val current = exoPlayer.currentPosition

        val target = (current + offsetMs)
            .coerceAtLeast(0L)
            .coerceAtMost(totalMills)

        exoPlayer.seekTo(target)
    }

    private fun startProgressTracker() {
        progressJob?.cancel()
        progressJob = viewModelScope.launch {
            while (true) {
                _uiState.update { it.copy(currentMills = exoPlayer.currentPosition) }
                delay(200L)
            }
        }
    }

    private fun sendAction(
        action: String,
        data: PlaybackNotificationData? = null
    ) {
        val intent = Intent(appContext, PlaybackService::class.java).apply {
            this.action = action
            data?.let {
                putExtra(EXTRA_URL, it.url)
                putExtra(EXTRA_TITLE, it.title)
                putExtra(EXTRA_ARTIST, it.artist)
                putExtra(EXTRA_ARTWORK, it.artwork)
            }
        }
        ContextCompat.startForegroundService(appContext, intent)
    }

    override fun onCleared() {
        progressJob?.cancel()
        exoPlayer.removeListener(playerListener)
        super.onCleared()
    }
}
