package com.alex.yang.hls_player.data.model

/**
 * Created by AlexYang on 2025/12/23.
 *
 *
 */
data class PlaybackNotificationData(
    val url: String = "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8",
    val title: String = "Big Buck Bunny",
    val artist: String = "AlexYang-影音頻道",
    val artwork: String = "https://storage.googleapis.com/gtv-videos-bucket/sample/images/BigBuckBunny.jpg"
)