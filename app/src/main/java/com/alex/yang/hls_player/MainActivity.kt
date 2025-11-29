package com.alex.yang.hls_player

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.alex.yang.hls_player.player.presentation.PlayerScreen
import com.alex.yang.hls_player.player.presentation.PlayerViewModel
import com.alex.yang.hls_player.ui.theme.AlexHLSPlayerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlexHLSPlayerTheme {
                val viewModel = hiltViewModel<PlayerViewModel>()

                LaunchedEffect(Unit) {
                    viewModel.setUpPlayer()
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PlayerScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}