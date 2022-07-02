package com.example.infystore.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.infystore.model.Product

@Database(entities = [Product::class],version=1)
abstract class ProductDatabase :RoomDatabase(){
    abstract  val productDao:ProductDao
    companion object{
        const val DATABASE_NAME="product_db"
    }

}