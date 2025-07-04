package com.spendscan.features.articles.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.spendscan.core.ui.components.ListItem
import com.spendscan.core.ui.components.SearchTextField
import com.spendscan.core.ui.components.TopBar
import com.spendscan.core.ui.theme.SpendScanTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment


@Composable
fun ArticleScreen(
    modifier: Modifier = Modifier,
    viewModel: ArticleViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopBar("Мои статьи")
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .fillMaxSize()
        ) {
            SearchTextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.searchText,
                onValueChange = { newText ->
                    viewModel.onSearchTextChange(newText)
                },
                onSearch = { query ->
                    viewModel.onSearchTextChange(query)
                },
                placeholder = "Найти статью"
            )

            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.tertiary)

            when {
                uiState.isLoading -> {
                    // Если идет загрузка, показываем индикатор прогресса
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Text(
                            text = "Загрузка статей...",
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                }
                uiState.error != null -> {
                    // Если произошла ошибка, показываем сообщение об ошибке
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Ошибка: ${uiState.error}",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                uiState.filteredArticles.isEmpty() && !uiState.isLoading && uiState.error == null && uiState.searchText.isNotBlank() -> {
                    // Если список пуст после фильтрации и есть текст поиска
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Статьи по запросу '${uiState.searchText}' не найдены.",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                uiState.filteredArticles.isEmpty() && !uiState.isLoading && uiState.error == null && uiState.searchText.isBlank() -> {
                    // Если список пуст изначально (нет загруженных статей)
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Список статей пуст.",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                else -> {
                    // В остальных случаях (когда есть данные), отображаем список
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(uiState.filteredArticles) { article ->
                            ListItem(
                                onClick = { /*TODO: Обработка клика по статье*/ },
                                leadingIconOrEmoji = article.emoji,
                                primaryText = article.name,
                                secondaryText = null,
                                trailingText = null,
                                trailingIcon = null,
                            )
                            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.tertiary)
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewArticle() {
    SpendScanTheme { ArticleScreen() }
}