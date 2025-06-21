package com.spendscan.features.expenses.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.spendscan.R
import com.spendscan.features.expenses.expensesList
import com.spendscan.features.expenses.myHistory.data.RetrofitClient
import com.spendscan.navigate.Route
import com.spendscan.ui.components.FloatingAddButton
import com.spendscan.ui.components.ListItem
import com.spendscan.ui.components.TopBar
import androidx.compose.runtime.getValue

@Composable
fun ExpensesScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    accountId: String = "1",
    viewModel: TodayExpensesViewModel = viewModel(
        factory = TodayExpensesViewModelFactory(RetrofitClient.myHistoryRepository, accountId)
    )

) {
    // Собираем состояния из ViewModel для отображения в UI
    val expenses by viewModel.transactions.collectAsState()
    val totalExpenses by viewModel.totalExpenses.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Box {
        Scaffold(
            modifier = modifier
                .background(color = MaterialTheme.colorScheme.background),
            topBar = {
                TopBar(
                    title = "Расходы сегодня",
                    actionIcon = ImageVector.vectorResource(id = R.drawable.history_icon),
                    onActionClick = { navController.navigate(Route.MyHistoryExpenses.route) }
                )
            }
        ) { innerPadding ->
            Column(modifier = Modifier.padding(top = innerPadding.calculateTopPadding())) {
                // Строка "Всего"
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(color = MaterialTheme.colorScheme.onSecondary),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Всего",
                        lineHeight = 24.sp,
                        fontSize = 16.sp,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier
                            .padding(start = 16.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = totalExpenses,
                        lineHeight = 24.sp,
                        fontSize = 16.sp,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier
                            .padding(end = 16.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.tertiary)


                // Обработка различных состояний данных: загрузка, ошибка, пустой список, или отображение данных
                when {
                    isLoading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Загрузка расходов...")
                        }
                    }
                    error != null -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Ошибка: ${error!!}")
                        }
                    }
                    expenses.isEmpty() -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Нет расходов за сегодня.")
                        }
                    }
                    else -> {
                        // LazyColumn для отображения списка расходов из ViewModel
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            items(expenses) { expense ->
                                ListItem(
                                    onClick = { /* TODO*/ },
                                    leadingIconOrEmoji = expense.category.emoji,
                                    primaryText = expense.category.name,
                                    secondaryText = expense.comment ?: "Без комментария",
                                    trailingText = "${expense.amount} ${expense.account.currency}",
                                )

                                HorizontalDivider(
                                    thickness = 1.dp,
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                            }
                        }
                    }
                }
            }
        }
        FloatingAddButton(
            onClick = { /*TODO*/ }
        )
    }
}