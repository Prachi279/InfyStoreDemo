package com.example.infystore.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.util.Log
import android.widget.Toast
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
            Log.d("MyForegroundSerive", "totalCount= " + totalCount)
            updatedCount = totalCount
            if (!CommonUtils.isOnline(this@MyForegrounndService2)) {
                dismissService()
            }
            if (updatedCount == 0) {
                dismissService()
                deleteRecords()
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
        delay(500L)
        stopForeground(true)
        stopSelf()
        Log.d("MyForegroundSerive", "Service Dismissed")
    }

    private suspend fun deleteRecords() {
        /* productDBRepository.deleteAllRecords()
         delay(500L)
         val count = productDBRepository.getTotalCount()
         Log.d("MyForegroundSerive", "DismissCount = " + count)*/
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
     * The callLimitedFunction meythod, to upload data in a chunk/based on query limit and delete records based on range
     */
    private suspend fun callLimitedFunction(id: Int) {
        if (CommonUtils.isOnline(this@MyForegrounndService2)) {
            var list = productDBRepository.getNextRecords(id)
            if (list.isNullOrEmpty()) {
                dismissService()
            }
            list.let {
                val listIds: List<Int> = list.map { item ->
                    item.id
                }
                //Log.d("MyForegroundSerive", "listIds =" + listIds)
                progressCount += calculateProgress(listIds.size)
                myRepository.getProductList().let { response ->
                    //Log.d("MyForegroundSerive", "listIds =" + listIds.size)
                    if (response.isSuccessful) {
                        updatedCount -= listIds.size
                        setUpProgress(progressCount)
                        val numOfDeletedRows: Int = productDBRepository.deleteRecorsInRange(
                            list[0].id,
                            list[listIds.size - 1].id
                        )
                        Log.d("MyForegroundSerive", "=================================")
                        Log.d("MyForegroundSerive", "list[0].id= " + list[0].id)
                        Log.d(
                            "MyForegroundSerive",
                            "list[listIds.size - 1].id = " + list[listIds.size - 1].id
                        )
                        Log.d(
                            "MyForegroundSerive",
                            "name " + list[listIds.size - 1].name
                        )
                        Log.d("MyForegroundSerive", "numOfDeletedRows = " + numOfDeletedRows)
                        Log.d("MyForegroundSerive", "=================================")
                        Log.d("MyForegroundSerive", "updatedCount =" + updatedCount)
                        if (updatedCount == 0) {
                            dismissService()
                            deleteRecords()
                        } else {
                            //Log.d("MyForegroundSerive", "NEXT_ID =" + list[listIds.size - 1].id)
                            callLimitedFunction(list[listIds.size - 1].id)
                        }
                    }
                }
            }

        } else {
            dismissService()
        }
    }

}