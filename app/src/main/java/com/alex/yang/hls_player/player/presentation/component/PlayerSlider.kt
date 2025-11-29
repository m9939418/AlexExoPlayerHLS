package com.alex.yang.hls_player.player.presentation.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alex.yang.hls_player.ui.theme.AlexHLSPlayerTheme
import kotlin.math.roundToInt

/**
 * Created by AlexYang on 2025/11/28.
 *
 *
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerSlider(
    modifier: Modifier = Modifier,
    currentMills: Long,
    totalMills: Long,
    onSeekTo: (Long) -> Unit,
) {
    // 安全處理 duration，避免 0 or 未知
    val safeDuration = totalMills.takeIf { it > 0 } ?: 1L

    // 把時間轉成 0f..1f 的進度
    val progress = (currentMills.toFloat() / safeDuration.toFloat())
        .coerceIn(0f, 1f)

    // 拖曳中的暫存值
    var sliderValue by remember(progress) { mutableStateOf(progress) }

    val colors = SliderDefaults.colors(
        thumbColor = Color(0XFFFBC92B),
        activeTrackColor = Color(0XFFFBC92B),
        inactiveTrackColor = Color(0X33000000),
    )

    Column(modifier = modifier.fillMaxWidth()) {
        Slider(
            modifier = Modifier
                .fillMaxWidth()
                .height(28.dp),
            colors = colors,
            // 自訂 Track（可調高度 + 圓角）
            track = { state ->
                // 方法一： 預設
//            SliderDefaults.Track(
//                sliderState = state,
//                colors = colors,
//                thumbTrackGapSize = 0.dp
//            )

                // 方法二： 客製化整個 Track（灰色底）
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .background(
                            color = Color.LightGray,
                            shape = CircleShape
                        )
                ) {
                    // 已播放區段（左邊那段黃的）
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(state.value)
                            .height(8.dp)
                            .background(
                                color = Color(0XFFFBC92B),
                                shape = CircleShape
                            )
                    )
                }
            },
            // 自訂 Thumb（立體感）
            thumb = {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .shadow(4.dp, CircleShape, clip = false)
                        .background(
                            color = Color(0XFFFBC92B),
                            shape = CircleShape
                        ),
                )
            },
            valueRange = 0f..1f,
            value = sliderValue,
            onValueChange = { sliderValue = it },
            onValueChangeFinished = {
                onSeekTo((sliderValue * safeDuration).toLong())
            },
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                color = Color.White,
                fontSize = 16.sp,
                text = formatTime(currentMills),
            )
            Text(
                color = Color.White,
                fontSize = 16.sp,
                text = formatTime(safeDuration),
            )
        }
    }
}

// 格式化 mm:ss formatter
private fun formatTime(ms: Long): String {
    if (ms <= 0) return "00:00"

    val totalSec = (ms / 1000f).roundToInt()
    return "%02d:%02d".format((totalSec / 60), totalSec % 60)
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
fun PlayerSliderPreview() {
    AlexHLSPlayerTheme {
        Scaffold { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .padding(paddingValues)
                    .padding(vertical = 50.dp)
            ) {
                PlayerSlider(
                    modifier = Modifier.padding(paddingValues),
                    currentMills = 90_000L,
                    totalMills = 300_000L,
                    onSeekTo = {}
                )
            }
        }
    }
}