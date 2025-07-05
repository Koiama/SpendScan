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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.spendscan.R
import com.spendscan.core.data.repository.TransactionRepositoryImpl
import com.spendscan.core.domain.repository.TransactionRepository
import com.spendscan.core.network.ConnectivityObserver
import com.spendscan.core.network.RetrofitClient
import com.spendscan.core.ui.components.FloatingAddButton
import com.spendscan.core.ui.components.ListItem
import com.spendscan.core.ui.components.TopBar
import com.spendscan.features.incomes.useCase.GetTodayIncomeUseCase
import com.spendscan.navigate.Route
import com.spendscan.core.domain.managers.GlobalCurrentAccountManager


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomesScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    val context = LocalContext.current

    val apiService = remember { RetrofitClient.transactionApiService }
    val repository: TransactionRepository = remember { TransactionRepositoryImpl(apiService) }
    val connectivityObserver = remember { ConnectivityObserver(context) }
    val getTodayIncomeUseCase = remember { GetTodayIncomeUseCase(repository) }

    val isAccountLoading by GlobalCurrentAccountManager.instance.isAccountLoading.collectAsState()
    val accountLoadError by GlobalCurrentAccountManager.instance.accountLoadError.collectAsState()
    val currentAccountId by GlobalCurrentAccountManager.instance.currentAccountId.collectAsState()
    val currentAccountName by GlobalCurrentAccountManager.instance.currentAccountName.collectAsState()

    val viewModel: TodayIncomeViewModel = viewModel(
        factory = TodayIncomeViewModelFactory(
            getTodayIncomeUseCase = getTodayIncomeUseCase,
            connectivityObserver = connectivityObserver
        )
    )

    val incomes by viewModel.transactions.collectAsState()
    val totalIncome by viewModel.totalIncome.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val isOnline by viewModel.isOnline.collectAsState()

    val screenTitle = remember(currentAccountName) {
        currentAccountName?.let { name ->
            if (name.isNotEmpty()) "$name - Доходы сегодня" else "Доходы сегодня"
        } ?: "Доходы сегодня"
    }

    Box {
        Scaffold(
            modifier = modifier,
            topBar = {
                TopBar(
                    title = screenTitle,
                    actionIcon = ImageVector.vectorResource(id = R.drawable.history_icon),
                    onActionClick = {
                        navController.navigate(
                            Route.MyHistory.createRoute(
                                isIncome = true,
                                title = "Мои доходы",
                                userAccountId = 23
                            )
                        )
                    }
                )
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(top = innerPadding.calculateTopPadding())
            ) {
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

                when {
                    isAccountLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                            Text("Загрузка данных аккаунта...", modifier = Modifier.padding(top = 70.dp), textAlign = TextAlign.Center)
                        }
                    }
                    accountLoadError != null -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Ошибка загрузки аккаунта: ${accountLoadError!!}",
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    currentAccountId == null -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Аккаунт не выбран или не загружен. Выберите аккаунт.", textAlign = TextAlign.Center)
                        }
                    }
                    !isOnline -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Нет подключения к интернету. Проверьте соединение.", textAlign = TextAlign.Center)
                        }
                    }

                    isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                            Text("Загрузка доходов...", modifier = Modifier.padding(top = 70.dp), textAlign = TextAlign.Center)
                        }
                    }

                    error != null -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Ошибка: ${error!!}",
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    incomes.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Нет доходов за сегодня.", textAlign = TextAlign.Center)
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
                                    secondaryText = income.comment,
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