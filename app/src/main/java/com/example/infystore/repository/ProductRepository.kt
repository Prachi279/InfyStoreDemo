package com.example.infystore.repository


import com.example.infystore.model.Product
import com.example.infystore.projectrepo.APIInterface
import javax.inject.Inject


/**
 * The ProductRepository class, to get the result of product
 * @Inject to provide instance of class
 */
class ProductRepository @Inject constructor(private val apiInterface: APIInterface) {
    suspend fun getProductList()=apiInterface.getAllList()
    suspend fun submitData(productList:List<Product>)=apiInterface.submitData(productList)
    suspend fun submitData()=apiInterface.submitData()
}