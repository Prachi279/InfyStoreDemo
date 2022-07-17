package com.example.infystore.utils

import com.example.infystore.BuildConfig

object Constants {
    const val END_POINT="products.json?brand=maybelline"
    const val MONGO_END_POINT="/submit"
    //const val MONGO_BASE_URL="http://192.168.0.102:3000/"
    const val MONGO_BASE_URL="http://10.0.2.2:3000"
    const val ORDER_LIST="order_list"
    const val IS_LOGGED_IN="is_loggged_in"
    const val LIMIT=10
    var baseUrl=BuildConfig.BASE_URL
}