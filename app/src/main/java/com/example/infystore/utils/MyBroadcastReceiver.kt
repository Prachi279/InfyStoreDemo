package com.example.infystore.utils

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startForegroundService

class MyBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        /*if(CommonUtils.isOnline(p0!!)){
            if (!CommonUtils.foregroundServiceRunning(p0)) {
                Log.d("MyForegroundSerive","internet is on")
                val serviceIntent = Intent(p0, MyForegrounndService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    p0.startForegroundService(serviceIntent)
                }
            }
        }else{
            Log.d("MyForegroundSerive","internet is off")
        }*/
    }
}