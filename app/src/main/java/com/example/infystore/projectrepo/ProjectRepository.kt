package com.example.infystore.projectrepo

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.infystore.BuildConfig
import com.example.infystore.db.ProductDao
import com.example.infystore.db.ProductDatabase
import com.example.infystore.repository.ProductRepository
import com.example.infystore.utils.Constants
import com.example.infystore.utils.PrefImpl
import com.example.infystore.utils.PreferenceHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
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
    fun getBaseUrl() = BuildConfig.BASE_URL

    @Provides
    @Singleton
    fun getHttpClient(): OkHttpClient{
        return OkHttpClient.Builder()
            .connectTimeout(2, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    //we can provide injection for this returned data types here APIInterface

    fun getRetrofitInstance(): APIInterface =
        Retrofit.Builder().baseUrl(Constants.baseUrl).client(getHttpClient())
            .addConverterFactory(
            GsonConverterFactory.create()
        )
            .build().create(APIInterface::class.java)

    //we can provide injection for this returned data types here sharedPreference
    @Provides
    @Singleton
    @Named("Pref")
    fun getPreferenceInstance(@ApplicationContext context: Context): SharedPreferences =
        PreferenceHelper.defaultPrefs(context)


    //we can provide injection for this returned data types here PrefImpl
    @Provides
    @Singleton
    fun getInstance(@ApplicationContext context: Context): PrefImpl =
        PrefImpl(getPreferenceInstance(context))


    //For workmanager testing purpose
    @Provides
    @Singleton
    fun getProductRepositoryInstance(@ApplicationContext context: Context): ProductRepository =
        ProductRepository(getRetrofitInstance())

    @Provides
    @Singleton
    fun getRoomDatabaseInstance(app: Application): ProductDatabase {
        return Room.databaseBuilder(app, ProductDatabase::class.java, ProductDatabase.DATABASE_NAME)
            .build()
    }

    @Provides
    @Singleton
    fun getProductDbRepositoryInstance(db: ProductDatabase): ProductDao {
        return db.productDao
    }
}