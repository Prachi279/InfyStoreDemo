package com.example.infystore.networkrepo

import com.example.infystore.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * The ProjectRepository, to create singleton class of retrofit
 * @Provides defines how and what type of  instance function should return or provide
 * @Module defines which object we need to provide injection
 * @InstallIn tells hilt that which class will be used by module
 */
@Module
@InstallIn(SingletonComponent::class)
object ProjectRepository {
    @Provides
    fun getBaseUrl()=BuildConfig.BASE_URL

    @Provides
    @Singleton
    fun getRetrofitInstance(): APIInterface = Retrofit.Builder().baseUrl(getBaseUrl()).addConverterFactory(
        GsonConverterFactory.create())
        .build().create(APIInterface::class.java)
}