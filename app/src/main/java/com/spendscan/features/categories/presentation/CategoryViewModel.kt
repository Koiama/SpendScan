package com.spendscan.features.categories.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spendscan.core.data.repository.CategoryRepositoryImpl
import com.spendscan.core.network.RetrofitClient
import com.spendscan.features.categories.domain.GetCategoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class CategoryViewModel(
    private val getCategoriesUseCase: GetCategoryUseCase =
        GetCategoryUseCase(
            categoryRepository = CategoryRepositoryImpl(
                categoriesApiService = RetrofitClient.categoriesApiService
            )
        )
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoryUiState())
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()

    init {
        loadCategories()
    }

    fun loadCategories() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            val result = getCategoriesUseCase.execute()
            result.fold(
                onSuccess = { categories ->
                    _uiState.value = _uiState.value.copy(
                        categories = categories,
                        filteredCategories = categories,
                        isLoading = false
                    )
                },
                onFailure = { throwable ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = throwable.localizedMessage ?: "Неизвестная ошибка"
                    )
                }
            )
        }
    }

    fun onSearchTextChange(query: String) {
        _uiState.value = _uiState.value.copy(searchText = query)
        filterCategories(query)
    }

    private fun filterCategories(query: String) {
        val currentCategories = _uiState.value.categories
        val filtered = if (query.isBlank()) {
            currentCategories
        } else {
            currentCategories.filter { category ->
                category.name.contains(query, ignoreCase = true)
            }
        }
        _uiState.value = _uiState.value.copy(filteredCategories = filtered)
    }
}