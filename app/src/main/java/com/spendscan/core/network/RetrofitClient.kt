package com.spendscan.core.network

import com.spendscan.core.data.api.TransactionApiService
import com.spendscan.core.data.repository.TransactionRepositoryImpl
import com.spendscan.core.domain.repository.TransactionRepository
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {


    private const val API_KEY = "U1lQnP6eu0Q9VrkjYdeOJyr2" //Так лучше не хардкодить

    // 1. Настройка JSON-парсера (Kotlinx Serialization)
    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        coerceInputValues = true
    }

    // 2. Настройка HTTP-клиента (OkHttpClient)
    // Логгер для просмотра сетевых запросов и ответов в Logcat.
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val authInterceptor = AuthInterceptor(API_KEY)

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(authInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private const val BASE_URL = "https://shmr-finance.ru/api/v1/"


    // 3. Создание экземпляра Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // 4. Создание экземпляра нашего API-сервиса, будет создан при первом обращении
    val transactionApiService: TransactionApiService by lazy {
        retrofit.create(TransactionApiService::class.java)
    }

    val myHistoryRepository: TransactionRepository by lazy {
        TransactionRepositoryImpl(transactionApiService)
    }
}