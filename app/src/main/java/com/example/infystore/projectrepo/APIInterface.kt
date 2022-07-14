package com.example.infystore.projectrepo

import com.example.infystore.model.CommonResponse
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

    @POST(Constants.MONGO_END_POINT)
    suspend fun submitData(@Body productList: List<Product>):Response<CommonResponse>

    @GET(Constants.MONGO_END_POINT)
    suspend fun submitData():Response<CommonResponse>
}