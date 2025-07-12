package com.spendscan.spendscan.feature.categories.ui.viewmodel.contract

import com.spendscan.spendscan.core.domain.models.transaction.Category

/**
 * Все возможные состояния UI при работе со списком категорий:
 */
data class CategoriesUiState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val categories: List<Category> = emptyList(),
    val filteredCategories: List<Category> = emptyList(),
    val error: String? = null
)
