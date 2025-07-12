package com.spendscan.spendscan.feature.categories.domain.usecase

import com.spendscan.spendscan.core.common.utils.Result
import com.spendscan.spendscan.core.domain.models.transaction.Category
import com.spendscan.spendscan.feature.categories.domain.repository.CategoriesRepository
import javax.inject.Inject

/**
 * UseCase получения списка категорий
 */
class GetCategoriesUseCase @Inject constructor(
    private val categoriesRepository: CategoriesRepository
) {
    suspend operator fun invoke(): Result<List<Category>> {
        return categoriesRepository.getCategories()
    }
}
