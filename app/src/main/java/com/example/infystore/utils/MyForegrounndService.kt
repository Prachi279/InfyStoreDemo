package com.example.infystore.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.infystore.R
import com.example.infystore.repository.ProductDBRepository
import com.example.infystore.repository.ProductRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class MyForegrounndService : Service() {
    @Inject
    lateinit var myRepository: ProductRepository

    @Inject
    lateinit var productDBRepository: ProductDBRepository
    private lateinit var notification: Notification.Builder

    private var updatedCount: Int = 0
    private var progressCount: Int = 0
    private var totalCount: Int = 0
    private lateinit var notificationManager: NotificationManager

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("MyForegroundSerive", "Service Started")
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("MyForegroundSerive", Thread.currentThread().toString())
            //Testing long operation
            /*  myRepository.getProductList().let { response ->
                  if (response.isSuccessful) {
                      Log.d("MyForegroundSerive", "My Service is running 7")
                  }
              }
              myRepository.getProductList().let { response ->
                  if (response.isSuccessful) {
                      Log.d("MyForegroundSerive", "My Service is running 7")
                  }
              }
              myRepository.getProductList().let { response ->
                  if (response.isSuccessful) {
                      Log.d("MyForegroundSerive", "My Service is running 7")
                  }
              }
              myRepository.getProductList().let { response ->
                  if (response.isSuccessful) {
                      Log.d("MyForegroundSerive", "My Service is running 7")
                  }
              }
              myRepository.getProductList().let { response ->
                  if (response.isSuccessful) {
                      Log.d("MyForegroundSerive", "My Service is running 7")
                  }
              }*/
            totalCount = productDBRepository.getTotalCount()
            updatedCount = totalCount
            Log.d("MyForegroundSerive", "updatedCount =" + updatedCount)
            if (updatedCount == 0) {
                dismissService()
            } else {
                callLimitedFunction()
            }
        }

        val channelId = "Foreground Service ID"
        val channel = NotificationChannel(
            channelId,
            channelId,
            NotificationManager.IMPORTANCE_LOW
        )

        notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
        notification = Notification.Builder(this, channelId)
            .setContentText("Uploading your data")
            .setContentTitle("InfyMe Service")
            .setSmallIcon(R.drawable.ic_home_black_24dp)
            .setProgress(100, 0, true)
        startForeground(1001, notification.build())

        return super.onStartCommand(intent, flags, startId)
    }

    private suspend fun dismissService() {
        val count = productDBRepository.getTotalCount()
        Log.d("MyForegroundSerive", "DismissCount = " + count)
        setUpProgress(100)
        delay(1000L)
        stopForeground(true)
        stopSelf()
        Log.d("MyForegroundSerive", "Service Dismissed")
    }

    private fun calculateProgress(size: Int): Int {
        return (100 * size) / totalCount
    }

    private fun setUpProgress(progress: Int) {
        Log.d("MyForegroundSerive", "progressCount =" + progressCount)
        notification.setProgress(100, progress, false)
        notificationManager.notify(1001, notification.build())
    }

    private suspend fun callLimitedFunction() {
        val list = productDBRepository.getLimitedData()
        if (list.isNullOrEmpty()) {
            dismissService()
        }
        list.let {
            val listIds: List<Int> = list.map { item -> item.id }
            progressCount += calculateProgress(listIds.size)
            myRepository.getProductList().let { response ->
                Log.d("MyForegroundSerive", "listIds =" + listIds)
                if (response.isSuccessful) {
                    productDBRepository.deleteProducts(listIds)
                    updatedCount -= listIds.size
                    setUpProgress(progressCount)
                    Log.d("MyForegroundSerive", "updatedCount =" + updatedCount)
                    if (updatedCount == 0) {
                        dismissService()
                    } else {
                        callLimitedFunction()
                    }
                }
            }
        }

    }

}