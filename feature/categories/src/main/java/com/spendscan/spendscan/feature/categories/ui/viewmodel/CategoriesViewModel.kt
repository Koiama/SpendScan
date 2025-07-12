package com.spendscan.spendscan.feature.categories.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.spendscan.spendscan.core.common.utils.Result
import com.spendscan.spendscan.core.common.utils.onError
import com.spendscan.spendscan.core.common.utils.onException
import com.spendscan.spendscan.core.common.utils.onSuccess
import com.spendscan.spendscan.core.domain.models.transaction.Category
import com.spendscan.spendscan.feature.categories.domain.usecase.GetCategoriesUseCase
import com.spendscan.spendscan.feature.categories.ui.viewmodel.contract.CategoriesScreenAction
import com.spendscan.spendscan.feature.categories.ui.viewmodel.contract.CategoriesUiState
import javax.inject.Inject

/**
 * ViewModel для экрана управления категориями (пока мок)
 */
class CategoriesViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<CategoriesUiState>(CategoriesUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getCategories()
            _uiState.map { it.searchQuery }
                .map { it.trim().lowercase() }
                .distinctUntilChanged()
                .collect { query ->
                    _uiState.update {
                        val newFilteredCategories = if (query.isBlank()) {
                            it.categories
                        } else {
                            it.categories.filter {
                                it.name.lowercase().contains(query)
                            }
                        }
                        it.copy(filteredCategories = newFilteredCategories)
                    }
                }
        }
    }

    fun onAction(action: CategoriesScreenAction) {
        when (action) {
            is CategoriesScreenAction.ChangeSearchQueryState -> _uiState.update {
                it.copy(searchQuery = action.newSearchQuery)
            }
        }
    }

    private suspend fun getCategories() {
        withContext(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true) }
            val result: Result<List<Category>> = getCategoriesUseCase()
            result.onSuccess { res ->
                _uiState.update { it.copy(isLoading = false, categories = res) }
            }.onError { code, message ->
                _uiState.update { it.copy(isLoading = false, error = message) }
            }.onException {
                Log.d("DEBUG", "mockCategories: $it")
            }
        }
    }
}
