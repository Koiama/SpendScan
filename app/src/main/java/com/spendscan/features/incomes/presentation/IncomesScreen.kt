package com.spendscan.features.incomes.presentation

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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.spendscan.R
import com.spendscan.features.expenses.myHistory.data.RetrofitClient
import com.spendscan.navigate.Route
import com.spendscan.ui.components.FloatingAddButton
import com.spendscan.ui.components.ListItem
import com.spendscan.ui.components.TopBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomesScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    accountId: String = "1",
    viewModel: TodayIncomeViewModel = viewModel(
        factory = TodayIncomeViewModelFactory(RetrofitClient.myHistoryRepository, accountId)
    )
) {
    val incomes by viewModel.transactions.collectAsState()
    val totalIncome by viewModel.totalIncome.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Box {
        Scaffold(
            modifier = modifier,
            topBar = {
                TopBar(
                    title = "Доходы сегодня",
                    actionIcon = ImageVector.vectorResource(id = R.drawable.history_icon),
                    onActionClick = { navController.navigate(Route.MyHistoryIncomes.route) }
                )
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(top = innerPadding.calculateTopPadding())
            ) {
                // Строка "Всего"
                Row(
                    modifier = Modifier
                        .height(56.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.onSecondary),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
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
                        text = totalIncome,
                        lineHeight = 24.sp,
                        fontSize = 16.sp,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier
                            .padding(end = 16.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.tertiary)

                // Обработка состояний (загрузка, ошибка, пустой список)
                when {
                    isLoading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            // Здесь может быть ваш индикатор загрузки
                            Text("Загрузка доходов...")
                        }
                    }
                    error != null -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Ошибка: ${error!!}")
                        }
                    }
                    incomes.isEmpty() -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Нет доходов за сегодня.")
                        }
                    }
                    else -> {

                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(incomes) { income ->
                                ListItem(
                                    onClick = { /*TODO*/ },
                                    leadingIconOrEmoji = income.category.emoji,
                                    primaryText = income.category.name,
                                    secondaryText = income.comment ,
                                    trailingText = "${income.amount} ${income.account.currency}",
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