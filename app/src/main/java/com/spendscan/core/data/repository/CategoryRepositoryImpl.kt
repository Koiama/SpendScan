package com.spendscan.core.data.repository


import com.spendscan.core.data.api.CategoriesApiService
import com.spendscan.core.data.mapper.toDomain
import com.spendscan.core.domain.models.Category
import com.spendscan.features.categories.domain.CategoryRepository

class CategoryRepositoryImpl(
    private val categoriesApiService: CategoriesApiService
) : CategoryRepository {

    override suspend fun getCategories(): Result<List<Category>> {
        return try {
            val response = categoriesApiService.getCategories()
            if (response.isSuccessful) {
                val categoryDtos = response.body() ?: emptyList()
                val categories = categoryDtos.map { dto ->
                    dto.toDomain()
                }
                Result.success(categories)
            } else {
                Result.failure(RuntimeException("Ошибка HTTP: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}