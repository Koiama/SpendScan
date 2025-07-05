package com.spendscan.core.data.api

import com.spendscan.core.data.models.CategoryDto
import retrofit2.Response
import retrofit2.http.GET

/**
 * API для загрузки статей
 */
interface CategoriesApiService {
    @GET("categories")
    suspend fun getCategories(
    ): Response<List<CategoryDto>>
}