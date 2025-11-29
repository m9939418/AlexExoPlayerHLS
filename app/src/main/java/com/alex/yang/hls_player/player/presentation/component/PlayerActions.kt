package com.alex.yang.hls_player.player.presentation.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alex.yang.hls_player.R
import com.alex.yang.hls_player.ui.theme.AlexHLSPlayerTheme

/**
 * Created by AlexYang on 2025/11/29.
 *
 *
 */
@Composable
fun PlayerAction(
    modifier: Modifier = Modifier,
    isPlaying: Boolean = false,
    onPrevClick: () -> Unit = {},
    onPrev10Click: () -> Unit = {},
    onPlayPauseClick: () -> Unit = {},
    onNext10Click: () -> Unit = {},
    onNextClick: () -> Unit = {},
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Previous 1s
        IconButton(
            modifier = Modifier.size(36.dp),
            onClick = onPrevClick
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_player_pre),
                contentDescription = null,
                tint = Color(0XFFFBC92B)
            )
        }

        // Previous 10s
        IconButton(
            modifier = Modifier.size(60.dp),
            onClick = onPrev10Click
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_player_pre_10),
                contentDescription = null,
                tint = Color(0XFFFBC92B)
            )
        }

        // Play | Pause
        IconButton(
            modifier = Modifier
                .size(72.dp)
                .background(color = Color(0XFFFBC92B), shape = CircleShape),
            onClick = onPlayPauseClick
        ) {
            Icon(
                painter = painterResource(if (isPlaying) R.drawable.ic_player_pause else R.drawable.ic_player_play),
                contentDescription = null,
                tint = Color.Black
            )
        }

        // Forward 10s
        IconButton(
            modifier = Modifier.size(60.dp),
            onClick = onNext10Click
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_player_next_10),
                contentDescription = null,
                tint = Color(0XFFFBC92B)
            )
        }

        // Forward 1s
        IconButton(
            modifier = Modifier.size(26.dp),
            onClick = onNextClick
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_player_next),
                contentDescription = null,
                tint = Color(0XFFFBC92B)
            )
        }
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
fun PlayerActionPreview() {
    AlexHLSPlayerTheme {
        PlayerAction()
    }
}