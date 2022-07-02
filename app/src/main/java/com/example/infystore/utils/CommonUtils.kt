package com.example.infystore.utils

import android.app.ActivityManager
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.infystore.R

/**
 * The CommonUtils method, to create general methods
 */
object CommonUtils {
    /**
     * The isOnline method, to check whether network is available or not
     * @param context
     * @return Boolean
     */
    fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT < 23) {
            val ni = cm.activeNetworkInfo
            if (ni != null) {
                return (ni.isConnected && (ni.type == ConnectivityManager.TYPE_WIFI || ni.type == ConnectivityManager.TYPE_MOBILE))
            }
        } else {
            val network = cm.activeNetwork
            if (network != null) {
                val nc = cm.getNetworkCapabilities(network)
                return (nc!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(
                    NetworkCapabilities.TRANSPORT_WIFI
                ))
            }
        }

        return false
    }

    /**
     * The showAlertDialog method, to show default alert dialog on logout and item purchase
     */
    fun showAlertDialog(
        view: View,
        message: String,
        positiveButtonClick: (DialogInterface, Int) -> Unit,
        negativeButtonClick: (DialogInterface, Int) -> Unit
    ) {
        val builder = AlertDialog.Builder(view.context)
        with(builder)
        {
            setTitle(context.resources.getString(R.string.app_name))
            setMessage(message)
            setPositiveButton(
                context.getString(R.string.proceed),
                DialogInterface.OnClickListener(positiveButtonClick)
            )
            setNegativeButton(context.getString(R.string.cancel), negativeButtonClick)
            show()
        }
    }

    fun foregroundServiceRunning(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in activityManager.getRunningServices(Int.MAX_VALUE)) {
            if (MyForegrounndService2::class.java.getName() == service.service.className) {
                return true
            }
        }
        return false
    }

}