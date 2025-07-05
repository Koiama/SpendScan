package com.spendscan.features.expenses.presentation

import ExpensesViewModel
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
import androidx.compose.material3.CircularProgressIndicator
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
import com.spendscan.core.network.RetrofitClient
import com.spendscan.navigate.Route
import com.spendscan.core.ui.components.FloatingAddButton
import com.spendscan.core.ui.components.ListItem
import com.spendscan.core.ui.components.TopBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.spendscan.core.data.repository.TransactionRepositoryImpl
import com.spendscan.core.domain.repository.TransactionRepository
import com.spendscan.core.network.ConnectivityObserver
import com.spendscan.features.expenses.useCase.GetTodayExpensesUseCase
import com.spendscan.core.domain.managers.GlobalCurrentAccountManager

@Composable
fun ExpensesScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val apiService = remember { RetrofitClient.transactionApiService }
    val repository: TransactionRepository = remember { TransactionRepositoryImpl(apiService) }
    val connectivityObserver = remember { ConnectivityObserver(context) }
    val getTodayExpensesUseCase = remember { GetTodayExpensesUseCase(repository) }

    // Получаем ID текущего аккаунта из менеджера
    val currentAccountId by GlobalCurrentAccountManager.instance.currentAccountId.collectAsState()
    val isAccountLoading by GlobalCurrentAccountManager.instance.isAccountLoading.collectAsState()
    val accountLoadError by GlobalCurrentAccountManager.instance.accountLoadError.collectAsState()

    // ViewModel будет инициализирован только после получения accountId
    val viewModel: ExpensesViewModel = viewModel(
        factory = ExpensesViewModelFactory(
            getTodayExpensesUseCase = getTodayExpensesUseCase,
            connectivityObserver = connectivityObserver
        )
    )

    val expenses by viewModel.transactions.collectAsState()
    val totalExpenses by viewModel.totalExpenses.collectAsState()
    val isLoadingExpenses by viewModel.isLoading.collectAsState()
    val errorExpenses by viewModel.error.collectAsState()
    val isOnline by viewModel.isOnline.collectAsState()

    Box {
        Scaffold(
            modifier = modifier
                .background(color = MaterialTheme.colorScheme.background),
            topBar = {
                TopBar(
                    title = "Расходы сегодня",
                    actionIcon = ImageVector.vectorResource(id = R.drawable.history_icon),
                    onActionClick = {
                        val accountIdToPass = GlobalCurrentAccountManager.instance.currentAccountId.value
                        val safeAccountId = accountIdToPass ?: 1
                        navController.navigate(Route.MyHistory.createRoute(isIncome = false, title = "Мои расходы", userAccountId = safeAccountId))
                    }
                )
            }
        ) { innerPadding ->
            Column(modifier = Modifier.padding(top = innerPadding.calculateTopPadding())) {
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

                when {
                    isAccountLoading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                            Text("Загрузка аккаунта...")
                        }
                    }
                    accountLoadError != null -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Ошибка загрузки аккаунта: ${accountLoadError!!}")
                        }
                    }
                    currentAccountId == null -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Ожидание ID аккаунта...")
                        }
                    }
                    !isOnline -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Нет подключения к интернету. Проверьте соединение.")
                        }
                    }
                    isLoadingExpenses -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                            Text("Загрузка расходов...")
                        }
                    }
                    errorExpenses != null -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Ошибка: ${errorExpenses!!}")
                        }
                    }
                    expenses.isEmpty() -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Нет расходов за сегодня.")
                        }
                    }
                    else -> {
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