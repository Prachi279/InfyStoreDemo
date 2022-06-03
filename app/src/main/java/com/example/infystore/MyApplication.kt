package com.example.infystore

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.infystore.utils.PreferenceHelper
import dagger.hilt.android.HiltAndroidApp

/**
 * The MyApplication class, base class of application
 * Parent component of the app thereby other app components can access dependency provided by it
 */
@HiltAndroidApp
class MyApplication : Application() {
    companion object {
        /**
         * The prefHelper, instance of SharedPreferences
         */
        var prefHelper: SharedPreferences? = null
    }

    override fun onCreate() {
        super.onCreate()
        getPreferenceInstance(this)
    }

    /**
     * The getPreferenceInstance method, to get singleton instance of SharedPreferences
     */
    private fun getPreferenceInstance(base: Context): SharedPreferences {
        if (prefHelper == null) {
            prefHelper = PreferenceHelper.defaultPrefs(base)
        }
        return prefHelper as SharedPreferences
    }
}