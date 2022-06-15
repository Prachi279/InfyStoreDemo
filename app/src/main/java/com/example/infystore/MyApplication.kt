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
class MyApplication : Application()