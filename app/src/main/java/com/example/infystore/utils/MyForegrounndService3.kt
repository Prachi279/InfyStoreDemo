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
import com.example.infystore.BuildConfig
import com.example.infystore.R
import com.example.infystore.repository.ProductDBRepository
import com.example.infystore.repository.ProductRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class MyForegrounndService3 : Service() {
    @Inject
    @Named("MongoInstace")
    lateinit var myRepository: ProductRepository

    @Inject
    lateinit var productDBRepository: ProductDBRepository
    private lateinit var notification: Notification.Builder

    private var totalCount: Int = 0
    private lateinit var notificationManager: NotificationManager
    var totalSheetCount = 0
    var sheetCount = 0
    var sheetNames = arrayOf("10", "15", "13", "11", "9")
    var i = 0
    var tempAllCount: Int = 0

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("MyForegroundSerive", "Service Started")
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("MyForegroundSerive", Thread.currentThread().toString())
            totalCount = productDBRepository.getTotalCount()
            Log.d("MyForegroundSerive", "totalCount= " + totalCount)
            totalSheetCount = getSheetCount(sheetNames[0])
            uploadData(0, sheetNames[0])

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

    private suspend fun uploadData(id: Int, sheetName: String) {
        Log.d("MyForegroundSerive", "name=" + sheetName)
        val productList = productDBRepository.getNextProductListByName(id, sheetName)
        Log.d("MyForegroundSerive", " productList[0].id=" +  productList[0].id)
        Log.d("MyForegroundSerive", " productList[productList.size - 1].id=" +  productList[productList.size - 1].id)
        if (productList.isEmpty()) {
            if (sheetCount == totalSheetCount) {
                sheetCount = 0
                doOnSheetComplete(id)
            }
        }
        myRepository.submitData(productList).let { response ->
            Log.d("MyForegroundSerive",""+response)
            if (response.code()==200) {
                val deleteNumberOfRecords = productDBRepository.deleteRecorsInRange(
                    productList[0].id,
                    productList[productList.size - 1].id
                )
                Log.d("MyForegroundSerive", " productList[productList.size - 1].id=" +  productList[productList.size - 1].id)
                sheetCount += deleteNumberOfRecords
                tempAllCount += deleteNumberOfRecords
                setUpProgress(calculateSheetProgress(tempAllCount), i, false)
            }
            if (sheetCount != totalSheetCount) {
                //Log.d("MyForegroundSerive", "sheetCount=" + sheetCount)
                uploadData(productList[productList.size - 1].id, sheetNames[i])
            } else if (sheetCount == totalSheetCount) {
                setUpProgress(calculateSheetProgress(tempAllCount), i, true)
                //Log.d("MyForegroundSerive", "sheetCount=" + sheetCount)
                sheetCount = 0;
                doOnSheetComplete(productList[productList.size - 1].id)
            }
        }
    }

    private suspend fun calculateSheetProgress(size: Int): Int {
        return (100 * size) / totalCount
    }

    private suspend fun doOnSheetComplete(id: Int) {
        if (tempAllCount != totalCount) {
            i += 1
            totalSheetCount = getSheetCount(sheetNames[i])
            uploadData(id, sheetNames[i])
        } else {
            dismissService()
        }
    }

    private suspend fun getSheetCount(sheetName: String): Int {
        return productDBRepository.getTotalCountOfSheet(sheetName)
    }

    private suspend fun dismissService() {
        var totalCount = productDBRepository.getTotalCount()
        delay(500L)
        stopForeground(true)
        stopSelf()
        Log.d("MyForegroundSerive", "Dismiss count=" + totalCount)
        Log.d("MyForegroundSerive", "Service Dismissed")
    }


    /**
     * The setUpProgress method, to setup notification progress
     */
    private fun setUpProgress(progress: Int, count: Int, boolean: Boolean) {
        notification.setProgress(100, progress, false)
        if (boolean) {
            notification.setContentText(count.toString() + " sheet uploaded")
        } else {
            notification.setContentText((sheetNames.size -count).toString()+ " sheet left")
        }
        notificationManager.notify(1001, notification.build())
    }

}