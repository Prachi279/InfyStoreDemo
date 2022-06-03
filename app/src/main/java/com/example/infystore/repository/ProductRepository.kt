package com.example.infystore.repository


import com.example.infystore.networkrepo.APIInterface
import javax.inject.Inject


/**
 * The ProductRepository class, to get the result of product
 * @Inject to provide instance of class
 */
class ProductRepository @Inject constructor(private val apiInterface: APIInterface) {
    suspend fun getProductList()=apiInterface.getAllList()
}