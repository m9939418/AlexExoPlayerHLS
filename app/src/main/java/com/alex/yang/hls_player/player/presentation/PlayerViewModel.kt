package com.alex.yang.hls_player.player.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
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
const val SAMPLE_HLS = "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8"

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val exoPlayer: ExoPlayer
) : ViewModel() {
    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState = _uiState.asStateFlow()

    private var hasInitialized = false
    private var progressJob: Job? = null

    val player: ExoPlayer
        get() = exoPlayer

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

        val mediaItem = MediaItem.Builder()
            .setUri(SAMPLE_HLS)
            .setMimeType(MimeTypes.APPLICATION_M3U8)
            .build()

        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true

        setUpPlayerTracker()
    }

    fun play() {
        exoPlayer.playWhenReady = true
    }

    fun pause() {
        exoPlayer.playWhenReady = false
    }

    fun togglePlayPause() {
        if (exoPlayer.isPlaying) {
            pause()
        } else {
            play()
        }
    }

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

    fun skipPrev1s() = skipBy(-1_000L)

    fun skipPrev10s() = skipBy(-10_000L)

    fun skipNext1s() = skipBy(1_000L)

    fun skipNext10s() = skipBy(10_000L)

    fun seekTo(ms: Long) {
        exoPlayer.seekTo(ms.coerceAtLeast(0L))
    }

    private fun setUpPlayerTracker() {
        progressJob?.cancel()
        progressJob = viewModelScope.launch {
            while (true) {
                _uiState.update { it.copy(currentMills = exoPlayer.currentPosition) }
                delay(200L)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        progressJob?.cancel()
        exoPlayer.removeListener(playerListener)
        exoPlayer.release()
    }
}
