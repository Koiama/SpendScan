package com.spendscan.features.categories.presentation

import com.spendscan.core.domain.models.Category


data class CategoryUiState(
    val categories: List<Category> = emptyList(),
    val filteredCategories: List<Category> = emptyList(),
    val searchText: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)