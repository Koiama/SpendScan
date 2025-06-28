package com.spendscan.features.articles.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spendscan.features.expenses.expensesList
import com.spendscan.core.ui.components.ListItem
import com.spendscan.core.ui.components.SearchTextField
import com.spendscan.core.ui.components.TopBar
import com.spendscan.core.ui.theme.SpendScanTheme

@Composable
fun ArticleScreen(modifier: Modifier = Modifier) {
    // Состояние для текста в поле поиска
    var searchText by remember { mutableStateOf("") }

    // Состояние для хранения отфильтрованного списка РАСХОДОВ
    var filteredExpensesList by remember { mutableStateOf(expensesList) }

    val onSearchPerformed: (String) -> Unit = { query ->
        if (query.isBlank()) {
            // Если строка поиска пустая, показываем весь список расходов
            filteredExpensesList = expensesList
        } else {
            // Иначе, фильтруем список расходов ТОЛЬКО ПО НАЗВАНИЮ ТРАТЫ (title)
            filteredExpensesList = expensesList.filter { expense ->
                expense.title.contains(query, ignoreCase = true)
            }
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopBar("Мои статьи")
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            SearchTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = searchText,
                onValueChange = { newText ->
                    searchText = newText
                    onSearchPerformed(newText) // Запускаем фильтрацию при каждом изменении текста
                },
                onSearch = onSearchPerformed, // Запускаем фильтрацию при нажатии Enter
                placeholder = "Найти статью"
            )


            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.tertiary)

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(filteredExpensesList) { expense ->
                    ListItem(
                        onClick = { /*TODO*/
                        },
                        leadingIconOrEmoji = expense.iconTag,
                        primaryText = expense.title,
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

@Preview
@Composable
fun PreviewArticle() {
    SpendScanTheme { ArticleScreen() }
}