package com.spendscan.spendscan.feature.categories.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import com.spendscan.spendscan.feature.categories.data.models.CategoryResponse

interface CategoriesApi {

    @GET("categories")
    suspend fun getAllCategories(): Response<List<CategoryResponse>>

    @GET("categories/type/{isIncome}")
    suspend fun getCategoriesByType(
        @Path("isIncome") isIncome: Boolean
    ): Response<List<CategoryResponse>>
}