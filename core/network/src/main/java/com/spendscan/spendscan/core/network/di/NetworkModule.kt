package com.spendscan.spendscan.core.network.di

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.spendscan.spendscan.core.common.utils.AppScope
import com.spendscan.spendscan.core.network.AuthInterceptor

@Module
class NetworkModule {

    @Provides
    @AppScope
    fun provideOkHttpClient(apiKey: String): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(apiKey))
            .build()

    @Provides
    @AppScope
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        val gson = GsonBuilder().serializeNulls().create()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    companion object {
        private const val BASE_URL = "https://shmr-finance.ru/api/v1/"
    }
}
