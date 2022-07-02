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
import com.example.infystore.model.Product
import com.example.infystore.repository.ProductDBRepository
import com.example.infystore.repository.ProductRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.util.Collections.list
import javax.inject.Inject

@AndroidEntryPoint
class MyForegrounndService2 : Service() {
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
            if (updatedCount == 0) {
                dismissService()
            } else {
                callLimitedFunction(0)
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
        productDBRepository.deleteAllRecords()
        delay(500L)
        val count = productDBRepository.getTotalCount()
        Log.d("MyForegroundSerive", "DismissCount = " + count)
        //setUpProgress(100)
        delay(500L)
        stopForeground(true)
        stopSelf()
        Log.d("MyForegroundSerive", "Service Dismissed")
    }

    /**
     * The calculateProgress method, to calculate progress of data uploading
     */
    private fun calculateProgress(size: Int): Int {
        return (100 * size) / totalCount
    }

    /**
     * The setUpProgress method, to setup notification progress
     */
    private fun setUpProgress(progress: Int) {
        Log.d("MyForegroundSerive", "progressCount =" + progressCount)
        notification.setProgress(100, progress, false)
        notificationManager.notify(1001, notification.build())
    }

    /**
     * Do not delete this method, this method is for to upload all the records of the table ex: 500000
     */
    /* private suspend fun callLimitedFunction() {
         var list = productDBRepository.getOfflineProductList()
         if (list.isNullOrEmpty()) {
             dismissService()
         }
         list.let {
             myRepository.getProductList().let { response ->
                 if (response.isSuccessful) {
                     Log.d("MyForegroundSerive", "Data uploaded successfully")
                     notification
                         .setContentText("Data uploaded successfully")
                         .setContentTitle("InfyMe Service")
                     notificationManager.notify(1001, notification.build())
                     dismissService()
                 }
             }
         }

     }*/

    /**
     * The callLimitedFunction meythod, to upload data in a chunk/based on query limit
     */
    private suspend fun callLimitedFunction(id: Int) {
        var list = productDBRepository.getNextRecords(id)
        if (list.isNullOrEmpty()) {
            dismissService()
        }
        list.let {
            val listIds: List<Int> = list.map { item -> item.id }
            //Log.d("MyForegroundSerive", "listIds =" + listIds)
            Log.d("MyForegroundSerive", "LASTId =" + list[listIds.size - 1].id)
            progressCount += calculateProgress(listIds.size)
            myRepository.getProductList().let { response ->
                //Log.d("MyForegroundSerive", "listIds =" + listIds.size)
                if (response.isSuccessful) {
                    updatedCount -= listIds.size
                    setUpProgress(progressCount)
                    Log.d("MyForegroundSerive", "updatedCount =" + updatedCount)
                    if (updatedCount == 0) {
                        dismissService()
                    } else {
                        Log.d("MyForegroundSerive", "NEXTID =" + list[listIds.size - 1].id)
                        callLimitedFunction(list[listIds.size - 1].id)
                    }
                }
            }
        }

    }

}