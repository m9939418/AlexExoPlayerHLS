package com.alex.yang.hls_player.player.presentation

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.ui.PlayerView
import com.alex.yang.hls_player.player.presentation.component.PlayInfo
import com.alex.yang.hls_player.player.presentation.component.PlayerAction
import com.alex.yang.hls_player.player.presentation.component.PlayerSlider
import com.alex.yang.hls_player.ui.theme.AlexHLSPlayerTheme

/**
 * Created by AlexYang on 2025/11/28.
 *
 *
 */
@Composable
fun PlayerScreen(
    modifier: Modifier = Modifier,
    viewModel: PlayerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val exoPlayer = viewModel.player

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .statusBarsPadding(),
    ) {
        AndroidView(
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f),
            factory = { context ->
                PlayerView(context).apply {
                    setPlayer(exoPlayer)
                    useController = true
                    setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 進度條 + 時間
        PlayerSlider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            currentMills = uiState.currentMills,
            totalMills = uiState.totalMills,
            onSeekTo = viewModel::seekTo,
        )

        Spacer(modifier = Modifier.height(30.dp))

        // 播放器按鈕組
        PlayerAction(
            isPlaying = uiState.isPlaying,
            onPrevClick = viewModel::skipPrev1s,
            onPrev10Click = viewModel::skipPrev10s,
            onPlayPauseClick = viewModel::togglePlayPause,
            onNext10Click = viewModel::skipNext10s,
            onNextClick = viewModel::skipNext1s,
        )

        Spacer(modifier = Modifier.height(24.dp))

        PlayInfo(data = viewModel.fakeNotificationData)
    }
}

@Preview(
    showBackground = true,
    showSystemUi = false,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "Light Mode"
)
@Preview(
    showBackground = true,
    showSystemUi = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Dark Mode"
)
@Composable
fun PlayerScreenPreview() {
    AlexHLSPlayerTheme {
        PlayerScreen()
    }
}