package com.spendscan.features.expenses.myHistory.data

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()// Получаем исходный запрос

        // Создаем новый запрос, добавляя заголовок Authorization
        val newRequest = originalRequest.newBuilder()
            .header("Authorization", token)
            .build()

        // Продолжаем цепочку запросов с новым запросом
        return chain.proceed(newRequest)
    }
}