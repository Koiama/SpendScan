package com.spendscan.features.categories.domain

import com.spendscan.core.domain.models.Category

interface CategoryRepository {
    suspend fun getCategories(): Result<List<Category>>
}