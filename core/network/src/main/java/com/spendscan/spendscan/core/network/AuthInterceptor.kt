package com.spendscan.spendscan.core.network

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor для добавления API-ключа в заголовки запросов.
 * Реализует интерфейс [Interceptor]
 */
class AuthInterceptor(private val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(request)
    }
}
