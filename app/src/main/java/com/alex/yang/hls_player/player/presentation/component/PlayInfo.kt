package com.alex.yang.hls_player.player.presentation.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alex.yang.hls_player.data.model.PlaybackNotificationData
import com.alex.yang.hls_player.ui.theme.AlexHLSPlayerTheme

/**
 * Created by AlexYang on 2025/12/24.
 *
 *
 */
@Composable
fun PlayInfo(
    modifier: Modifier = Modifier,
    data: PlaybackNotificationData
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E).copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                text = "Playback Info"
            )

            Spacer(modifier = Modifier.height(2.dp))

            InfoRow(label = "Title", value = data.title)
            InfoRow(label = "Artist", value = data.artist)
            InfoRow(label = "URL", value = data.url)
            InfoRow(label = "Image", value = data.artwork)
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
fun PlayInfoPreview() {
    AlexHLSPlayerTheme {
        PlayInfo(
            data = PlaybackNotificationData()
        )
    }
}
