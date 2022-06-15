package com.example.infystore.networkrepo

import android.content.Context
import android.content.SharedPreferences
import com.example.infystore.BuildConfig
import com.example.infystore.utils.PrefImpl
import com.example.infystore.utils.PreferenceHelper
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
object ProjectRepository {
    @Provides
    fun getBaseUrl()=BuildConfig.BASE_URL

    //we can provide injection for this returned data types here APIInterface
    @Provides
    @Singleton
    fun getRetrofitInstance(): APIInterface = Retrofit.Builder().baseUrl(getBaseUrl()).addConverterFactory(
        GsonConverterFactory.create())
        .build().create(APIInterface::class.java)

    //we can provide injection for this returned data types here sharedPreference
    @Provides
    @Singleton
    @Named("Pref")
    fun getPreferenceInstance(@ApplicationContext context: Context): SharedPreferences = PreferenceHelper.defaultPrefs(context)


    //we can provide injection for this returned data types here PrefImpl
    @Provides
    @Singleton
    fun getInstance(@ApplicationContext context: Context): PrefImpl=PrefImpl(getPreferenceInstance(context))
}