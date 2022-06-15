package com.example.infystore.utils

import android.content.SharedPreferences
import android.text.TextUtils
import com.example.infystore.model.Product
import com.example.infystore.utils.PreferenceHelper.get
import com.example.infystore.utils.PreferenceHelper.set
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

/**
 * The PrefImpl class, to provide injection for sharedpreferences and to save data
 * data injection not possible for (object class) and companion variables or static java variables
 * Therefore here we made separate class PrefImpl
 */
class PrefImpl @Inject constructor(var applicationPref: SharedPreferences) {

    /**
     * The saveObjIntoPref method, to save object/arraylist into preference
     */
    fun <T> saveObjIntoPref(data: T, prefName: String) {
        val gson = Gson()
        val json: String = gson.toJson(data)
        applicationPref[prefName] = json
    }
    /**
     * The getObjFromPref method, to get object from the preference
     */
    fun <T> getObjFromPref(prefName: String, className: Class<T>): T? {
        val gson = Gson()
        val data: String = applicationPref[prefName, ""]!!
        return if (!TextUtils.isEmpty(data)) {
            val obj = gson.fromJson(data, className)
            obj
        } else {
            null
        }
    }
    /**
     * The getArrayListFromPref method, to get saved arraylist object from the preference
     */
    fun getArrayListFromPref(prefName: String): List<Product>? {
        val gson = Gson()
        val data: String? = applicationPref[prefName, ""]!!
        if (!TextUtils.isEmpty(data)) {
            val type = object : TypeToken<List<Product?>?>() {}.type
            return gson.fromJson(data, type)
        }
        return null
    }
}