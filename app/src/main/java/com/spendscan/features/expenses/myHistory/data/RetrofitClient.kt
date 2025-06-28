package com.spendscan.features.expenses.myHistory.data

import okhttp3.logging.HttpLoggingInterceptor
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import okhttp3.MediaType.Companion.toMediaType
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.spendscan.features.expenses.myHistory.data.api.TransactionApiService
import com.spendscan.features.expenses.myHistory.domain.MyHistoryRepository
import com.spendscan.features.expenses.myHistory.domain.MyHistoryRepositoryImpl
import java.util.concurrent.TimeUnit

object RetrofitClient {



    private const val AUTH_TOKEN = "Bearer U1lQnP6eu0Q9VrkjYdeOJyr2" //Так лучше не хардкодить

    // 1. Настройка JSON-парсера (Kotlinx Serialization)
    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        coerceInputValues = true
    }

    // 2. Настройка HTTP-клиента (OkHttpClient)
    // Логгер для просмотра сетевых запросов и ответов в Logcat.
    private val loggingInterceptor = HttpLoggingInterceptor().apply{
        level = HttpLoggingInterceptor.Level.BODY // Логировать заголовки и тело запроса/ответа.
    }

    private val authInterceptor = AuthInterceptor(AUTH_TOKEN)

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor) // Добавляем логер
        .addInterceptor(authInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS) // Устанавливаем таймаут соединения
        .readTimeout(30, TimeUnit.SECONDS) // Таймаут на чтение данных
        .writeTimeout(30, TimeUnit.SECONDS) // Таймаут на запись данных
        .build()

    // 3. Базовый URL
    // Все относительные пути из API-интерфейсов будут добавляться к этому URL.
    private const val BASE_URL = "https://shmr-finance.ru/api/v1/"



    // 4. Создание экземпляра Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType())) //Указываем, как конвертировать JSON в Kotlin-объекты и обратно.
        .build()

    // 5. Создание экземпляра нашего API-сервиса, будет создан при первом обращении
    val transactionApiService: TransactionApiService by lazy {
        retrofit.create(TransactionApiService::class.java)
    }

    val myHistoryRepository: MyHistoryRepository by lazy {
        MyHistoryRepositoryImpl(transactionApiService)
    }
}