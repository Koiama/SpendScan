package com.spendscan.features.articles.presentation

import com.spendscan.features.articles.domain.Article

data class ArticleUiState(
    val articles: List<Article> = emptyList(),
    val filteredArticles: List<Article> = emptyList(),
    val searchText: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
