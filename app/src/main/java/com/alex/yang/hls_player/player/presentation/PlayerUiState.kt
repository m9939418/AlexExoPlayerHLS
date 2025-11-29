package com.alex.yang.hls_player.player.presentation

/**
 * Created by AlexYang on 2025/11/29.
 *
 *
 */
data class PlayerUiState(
    val currentMills: Long = 0L,
    val totalMills: Long = 0L,
    val isPlaying: Boolean = false,
)