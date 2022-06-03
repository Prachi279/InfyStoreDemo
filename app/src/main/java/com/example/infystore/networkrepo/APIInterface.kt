package com.example.infystore.networkrepo
import com.example.infystore.model.Product
import com.example.infystore.utils.Constants
import retrofit2.Response
import retrofit2.http.*

/**
 * The APIInterface interface, to call APIs
 */
interface APIInterface {
    @GET(Constants.END_POINT)
    suspend fun getAllList(): Response<List<Product>>
}