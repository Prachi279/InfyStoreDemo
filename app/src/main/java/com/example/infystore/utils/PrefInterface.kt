package com.example.infystore.utils

import com.example.infystore.model.Product

interface PrefInterface {
   fun<T> saveObjIntoPref(data:T, prefName:String)
   fun<T> getObjFromPref(prefName: String, className: Class<T>):T?
   fun getArrayListFromPref(prefName: String): List<Product>?
}