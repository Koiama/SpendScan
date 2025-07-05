package com.spendscan.features.categories.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment


@Composable
fun CategoryScreen( // Переименовано в CategoryScreen
    modifier: Modifier = Modifier,
    viewModel: CategoryViewModel = viewModel() // Используем CategoryViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopBar("Мои категории") // Изменен текст в TopBar
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
                placeholder = "Найти категорию" // Изменен плейсхолдер
            )

            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.tertiary)

            when {
                uiState.isLoading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Text(
                            text = "Загрузка категорий...", // Изменен текст
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                }
                uiState.error != null -> {
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
                uiState.filteredCategories.isEmpty() && !uiState.isLoading && uiState.error == null && uiState.searchText.isNotBlank() -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Категории по запросу '${uiState.searchText}' не найдены.", // Изменен текст
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                uiState.filteredCategories.isEmpty() && !uiState.isLoading && uiState.error == null && uiState.searchText.isBlank() -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Список категорий пуст.", // Изменен текст
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(uiState.filteredCategories) { category -> // Используем filteredCategories и category
                            ListItem(
                                onClick = { /*TODO: Обработка клика по категории*/ },
                                leadingIconOrEmoji = category.emoji, // Используем category.emoji
                                primaryText = category.name, // Используем category.name
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
fun PreviewCategoryScreen() { // Изменено имя Preview
    SpendScanTheme { CategoryScreen() } // Используем CategoryScreen
}