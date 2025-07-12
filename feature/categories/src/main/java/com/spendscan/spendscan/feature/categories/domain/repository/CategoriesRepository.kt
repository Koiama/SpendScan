package com.spendscan.spendscan.feature.categories.domain.repository

import com.spendscan.spendscan.core.common.utils.Result
import com.spendscan.spendscan.core.domain.models.transaction.Category

/**
 * Интерфейс, описывающий бизнес-правила получения категорий
 */
interface CategoriesRepository {
    suspend fun getCategories(): Result<List<Category>>
    suspend fun getCategoriesByType(isIncome: Boolean): Result<List<Category>>
}
