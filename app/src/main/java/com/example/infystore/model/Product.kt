package com.example.infystore.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * The Product, A data class
 */
@Entity
data class Product(
    @PrimaryKey val id: Int,
    val name: String,
    val price: String,
    val image_link: String,
    val description: String,
    var isPurchased: Boolean = false
)
