package com.spendscan.features.myHistory.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize // <-- Добавлен импорт
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
import androidx.compose.ui.Alignment // <-- Добавлен импорт
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.spendscan.R
import com.spendscan.core.common.formatAmount
import com.spendscan.core.common.toFormattedTime
import com.spendscan.core.data.repository.TransactionRepositoryImpl
import com.spendscan.core.domain.repository.TransactionRepository
import com.spendscan.core.network.RetrofitClient
import com.spendscan.core.ui.components.ListItem
import com.spendscan.core.ui.components.TopBar
import com.spendscan.core.network.ConnectivityObserver
import java.time.Instant
import java.time.ZoneId
import com.spendscan.core.domain.managers.GlobalCurrentAccountManager // <-- ИМПОРТ МЕНЕДЖЕРА

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionListScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    isIncome: Boolean? = null,
    title: String // <-- Если хотите использовать название из менеджера, это поле можно удалить
) {
    val context = LocalContext.current

    // Инициализация репозитория и ConnectivityObserver
    val apiService = remember { RetrofitClient.transactionApiService }
    val repository: TransactionRepository = remember { TransactionRepositoryImpl(apiService) }
    val connectivityObserver = remember { ConnectivityObserver(context) }

    // Собираем состояния из GlobalCurrentAccountManager
    val isAccountLoading by GlobalCurrentAccountManager.instance.isAccountLoading.collectAsState()
    val accountLoadError by GlobalCurrentAccountManager.instance.accountLoadError.collectAsState()
    val currentAccountId by GlobalCurrentAccountManager.instance.currentAccountId.collectAsState()
    val currentAccountName by GlobalCurrentAccountManager.instance.currentAccountName.collectAsState() // <-- Получаем название аккаунта

    // ViewModel теперь инициализируется без accountId, который ViewModel будет брать из GlobalCurrentAccountManager
    val viewModel: MyHistoryViewModel = viewModel(
        factory = MyHistoryViewModelFactory(
            repository = repository,
            isIncome = isIncome,
            connectivityObserver = connectivityObserver
        )
    )

    val transactions by viewModel.transactions.collectAsState()
    val isLoadingTransactions by viewModel.isLoading.collectAsState() // Переименовано для ясности
    val errorTransactions by viewModel.error.collectAsState() // Переименовано для ясности
    val startDate by viewModel.startDate.collectAsState()
    val endDate by viewModel.endDate.collectAsState()
    val totalFormatted by viewModel.totalFormatted.collectAsState()
    val isOnline by viewModel.isOnline.collectAsState()

    // Динамический заголовок
    val screenTitle = remember(currentAccountName, title) {
        // Если название аккаунта есть, используем его, иначе используем переданный title
        currentAccountName?.let { name ->
            if (name.isNotEmpty()) "$name - $title" else title // Можно настроить формат заголовка
        } ?: title
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopBar(
                title = screenTitle, // <-- Используем динамический заголовок
                actionIcon = ImageVector.vectorResource(R.drawable.trailing_icon),
                onActionClick = {/*TODO*/ },
                backButton = ImageVector.vectorResource(R.drawable.back_icon),
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(top = innerPadding.calculateTopPadding())) {

            // --- ОБРАБОТКА СОСТОЯНИЯ ЗАГРУЗКИ АККАУНТА ---
            if (isAccountLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                    Text("Загрузка данных аккаунта...")
                }
                return@Column // Выходим, пока аккаунт загружается
            }

            if (accountLoadError != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Ошибка загрузки аккаунта: ${accountLoadError!!}", color = MaterialTheme.colorScheme.error)
                }
                return@Column // Выходим, если есть ошибка загрузки аккаунта
            }

            if (currentAccountId == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Ожидание ID аккаунта...")
                }
                return@Column // Выходим, если ID аккаунта еще не доступен
            }

            // Только если аккаунт успешно загружен и ID доступен, продолжаем отображение остального содержимого

            val currentStartDateMillis = remember(startDate) {
                startDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            }
            val currentEndDateMillis = remember(endDate) {
                endDate.atTime(23, 59, 59, 999999999).atZone(ZoneId.systemDefault()).toInstant()
                    .toEpochMilli()
            }

            DateSelectionSection(
                startDateMillis = currentStartDateMillis,
                endDateMillis = currentEndDateMillis,
                onStartDateSelected = { selectedDateMillis ->
                    selectedDateMillis?.let {
                        val date =
                            Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                        viewModel.setDateRange(date, endDate)
                    }
                },
                onEndDateSelected = { selectedDateMillis ->
                    selectedDateMillis?.let {
                        val date =
                            Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                        viewModel.setDateRange(startDate, date)
                    }
                }
            )

            ListItem(
                primaryText = "Сумма",
                itemBackgroundColor = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.height(55.dp),
                trailingText = totalFormatted,
                trailingIcon = null
            )
            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.tertiary)

            // --- КОММУНИКАЦИЯ С ПОЛЬЗОВАТЕЛЕМ О СОСТОЯНИИ СЕТИ И ОШИБКАХ ТРАНЗАКЦИЙ ---
            if (!isOnline) {
                Text(
                    text = "Нет подключения к интернету. Проверьте соединение.",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    textAlign = TextAlign.Center
                )
            } else if (errorTransactions != null) {
                Text(
                    text = "Ошибка загрузки: ${errorTransactions}!",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    textAlign = TextAlign.Center
                )
            } else if (isLoadingTransactions) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            } else if (transactions.isEmpty()) {
                Text(
                    text = "Нет транзакций за выбранный период.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            } else {
                LazyColumn() {
                    items(transactions) { transaction ->
                        ListItem(
                            onClick = { /*TODO*/ },
                            primaryText = "${transaction.category.name}",
                            secondaryText = "${transaction.comment}",
                            leadingIconOrEmoji = "${transaction.category.emoji}",
                            trailingText = formatAmount(
                                amount = transaction.amount,
                                currencyCode = transaction.account.currency
                            ),
                            secondTrailingText = "${toFormattedTime(transaction.date)}",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(69.dp)
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