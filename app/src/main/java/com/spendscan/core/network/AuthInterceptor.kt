package com.spendscan.core.network

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor для добавления API-ключа в заголовки запросов
 */

class AuthInterceptor(private val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()
        return chain.proceed(newRequest)
    }
}