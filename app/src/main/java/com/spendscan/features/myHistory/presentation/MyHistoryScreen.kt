package com.spendscan.features.myHistory.presentation

import androidx.compose.foundation.layout.Column
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionListScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    userAccountId: Int = 1,
    isIncome: Boolean? = null,
    title: String
) {
    val context = LocalContext.current

    // Инициализация репозитория и ConnectivityObserver
    val apiService = remember { RetrofitClient.transactionApiService }
    val repository: TransactionRepository = remember { TransactionRepositoryImpl(apiService) }
    val connectivityObserver = remember { ConnectivityObserver(context) }

    val viewModel: MyHistoryViewModel = viewModel(
        factory = MyHistoryViewModelFactory(
            repository = repository,
            accountId = userAccountId,
            isIncome = isIncome,
            connectivityObserver = connectivityObserver
        )
    )

    val transactions by viewModel.transactions.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val startDate by viewModel.startDate.collectAsState()
    val endDate by viewModel.endDate.collectAsState()
    val totalFormatted by viewModel.totalFormatted.collectAsState()
    val isOnline by viewModel.isOnline.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopBar(
                title = title,
                actionIcon = ImageVector.vectorResource(R.drawable.trailing_icon),
                onActionClick = {/*TODO*/ },
                backButton = ImageVector.vectorResource(R.drawable.back_icon),
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(top = innerPadding.calculateTopPadding())) {

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

            // --- КОММУНИКАЦИЯ С ПОЛЬЗОВАТЕЛЕМ О СОСТОЯНИИ СЕТИ И ОШИБКАХ ---
            if (!isOnline) {
                Text(
                    text = "Нет подключения к интернету. Проверьте соединение.",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    textAlign = TextAlign.Center
                )
            } else if (error != null) {
                Text(
                    text = "Ошибка загрузки: ${error}!",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    textAlign = TextAlign.Center
                )
            } else if (isLoading) {
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