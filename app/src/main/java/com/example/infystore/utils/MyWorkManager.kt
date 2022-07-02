package com.example.infystore.utils

import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.infystore.R
import com.example.infystore.repository.ProductRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.random.Random

class MyWorkManager(private var context: Context, private val workParams: WorkerParameters) :
    CoroutineWorker(context, workParams) {

    @Inject
    lateinit var myRepository: ProductRepository

    override suspend fun doWork(): Result {
        //showNotification()
        delay(5000L)
        myRepository.getProductList().let { response ->
            if (response.isSuccessful) {
                Log.d("MyForegroundSerive", "My Service is running 2")
                return Result.success()
            } else {
                return Result.failure()
            }
        }
    }

    private suspend fun showNotification() {
        setForeground(
            ForegroundInfo(
                Random.nextInt(), NotificationCompat.Builder(context, "Download data").setSmallIcon(
                    R.drawable.ic_home_black_24dp
                ).setContentText("Downloading..").setContentTitle("Downloading progress").build()
            )
        )
    }
}