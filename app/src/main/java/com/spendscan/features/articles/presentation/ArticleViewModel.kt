package com.spendscan.features.articles.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spendscan.features.articles.domain.GetArticlesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ArticleViewModel(
    private val getArticlesUseCase: GetArticlesUseCase = GetArticlesUseCase() // DI-зависимость
) : ViewModel() {

    private val _uiState = MutableStateFlow(ArticleUiState())
    val uiState: StateFlow<ArticleUiState> = _uiState.asStateFlow()

    init {
        loadArticles()
    }

    fun loadArticles() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            val result = getArticlesUseCase.execute()
            result.fold(
                onSuccess = { articleRepo -> // Получаем ArticleRepo здесь
                    _uiState.value = _uiState.value.copy(
                        articles = articleRepo.categories, // Извлекаем список из .categories
                        filteredArticles = articleRepo.categories,
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
        filterArticles(query)
    }

    private fun filterArticles(query: String) {
        val currentArticles = _uiState.value.articles
        val filtered = if (query.isBlank()) {
            currentArticles
        } else {
            currentArticles.filter { article ->
                article.name.contains(query, ignoreCase = true)
            }
        }
        _uiState.value = _uiState.value.copy(filteredArticles = filtered)
    }
}
