package com.spendscan.spendscan.feature.manage_transaction.domain.usecase

import com.spendscan.spendscan.feature.categories.domain.repository.CategoriesRepository
import javax.inject.Inject

class GetCategoriesByTypeUseCase @Inject constructor(
    private val categoriesRepository: CategoriesRepository
) {
    suspend operator fun invoke(isIncome: Boolean) =
        categoriesRepository.getCategoriesByType(isIncome)
}