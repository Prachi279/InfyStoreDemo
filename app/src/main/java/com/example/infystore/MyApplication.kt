package com.example.infystore

import android.R
import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.infystore.utils.PreferenceHelper
import dagger.hilt.android.HiltAndroidApp

/**
 * The MyApplication class, base class of application
 * Parent component of the app thereby other app components can access dependency provided by it
 */
@HiltAndroidApp
class MyApplication : Application(){
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()

        //For work manager testing
      /*  val CHANNELID = "Foreground Service ID"
        val channel = NotificationChannel(
            CHANNELID,
            CHANNELID,
            NotificationManager.IMPORTANCE_LOW
        )

        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)*/
    }
}