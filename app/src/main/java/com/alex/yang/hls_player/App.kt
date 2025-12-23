package com.alex.yang.hls_player

import android.app.Application
import com.alex.yang.hls_player.data.notification.AppNotificationChannel
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * Created by AlexYang on 2025/11/28.
 *
 *
 */
@HiltAndroidApp
class App : Application() {
    @Inject
    lateinit var appNotificationChannel: AppNotificationChannel

    override fun onCreate() {
        super.onCreate()

        appNotificationChannel.init()
    }
}