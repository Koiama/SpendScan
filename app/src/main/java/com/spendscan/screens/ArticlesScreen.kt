package com.spendscan.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spendscan.data.models.expensesList
import com.spendscan.ui.components.ListItem
import com.spendscan.ui.components.SearchTextField
import com.spendscan.ui.theme.SpendScanTheme

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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "Мои статьи",
                        lineHeight = 28.sp,
                        fontSize = 22.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        letterSpacing = 0.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
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