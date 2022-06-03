package com.example.infystore.model

/**
 * The Product, A data class
 */
data class Product(val id:Int,val name:String,val price:String,val image_link:String,val description:String,
                   var isPurchased:Boolean=false)
