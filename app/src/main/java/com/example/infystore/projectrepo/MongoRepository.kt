package com.example.infystore.projectrepo

import android.content.Context

import com.example.infystore.repository.ProductRepository
import com.example.infystore.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

/**
 * The ProjectRepository, to create singleton class of retrofit
 * @Provides defines how and what type of  instance function should return or provide
 * @Module defines which object we need to provide injection
 * @InstallIn tells hilt that which class will be used by module
 */
@Module
@InstallIn(SingletonComponent::class)
object MongoRepository {

    //we can provide injection for this returned data types here APIInterface
    @Provides
    @Singleton
    fun getRetrofitInstance2(): APIInterface =
        Retrofit.Builder().baseUrl( Constants.MONGO_BASE_URL).addConverterFactory(
            GsonConverterFactory.create()
        )
            .build().create(APIInterface::class.java)



    //For workmanager testing purpose
    @Provides
    @Singleton
    @Named("MongoInstace")
    fun getProductRepositoryInstance2(@ApplicationContext context: Context): ProductRepository =
        ProductRepository(getRetrofitInstance2())
}