package com.alex.yang.hls_player.player.presentation.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alex.yang.hls_player.ui.theme.AlexHLSPlayerTheme

/**
 * Created by AlexYang on 2025/12/24.
 *
 *
 */
@Composable
fun InfoRow(
    modifier: Modifier = Modifier,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.2f),
            style = MaterialTheme.typography.labelMedium,
            color = Color.White.copy(alpha = 0.8f),
            text = label
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.8f),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            text = value
        )
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
fun InfoRowPreview() {
    AlexHLSPlayerTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Black)
                .padding(16.dp)
        ) {
            InfoRow(
                label = "Resolution",
                value = "1920x1080"
            )
        }
    }
}