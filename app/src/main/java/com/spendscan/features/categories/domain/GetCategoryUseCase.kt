package com.spendscan.features.categories.domain

import com.spendscan.core.domain.models.Category

// UseCase для получения статей
// Этот класс будет отвечать за бизнес-логику получения данных,
// заглушка.
class GetCategoryUseCase(
    private val categoryRepository: CategoryRepository // Теперь принимаем CategoryRepository
) {
    suspend fun execute(): Result<List<Category>> { // Возвращаем List<Category>
        return categoryRepository.getCategories() // Вызываем getCategories()
    }
}