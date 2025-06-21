package com.spendscan.features.expenses.myHistory.presentation

import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.spendscan.R
import com.spendscan.ui.components.ListItem
import com.spendscan.ui.components.TopBar
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.material3.ExperimentalMaterial3Api
import com.spendscan.features.expenses.myHistory.data.RetrofitClient
import com.spendscan.features.expenses.myHistory.data.models.formatAmountSimple
import com.spendscan.features.expenses.myHistory.data.models.toFormattedTime


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyHistoryScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    userAccountId: String = "1", // Передаем accountId прямо здесь

    viewModel: MyHistoryViewModel = viewModel(
        factory = MyHistoryViewModelFactory(RetrofitClient.myHistoryRepository, userAccountId) // !!! Передаем userAccountId в фабрику
    )
) {
    val transactions by viewModel.transactions.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val startDate by viewModel.startDate.collectAsState()
    val endDate by viewModel.endDate.collectAsState()
    val totalExpenses by viewModel.totalExpenses.collectAsState()



    Scaffold(
        modifier = modifier,
        topBar = {
            TopBar(
                title = "Моя история",
                actionIcon = ImageVector.vectorResource(R.drawable.trailing_icon),
                onActionClick = {/*TODO*/ },
                backButton = ImageVector.vectorResource(R.drawable.back_icon),
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(top = innerPadding.calculateTopPadding())) {

            DateSelectionSection(
                startDateMillis = startDate,
                endDateMillis = endDate,
                onStartDateSelected = { selectedDate -> viewModel.setStartDate(selectedDate) },
                onEndDateSelected = { selectedDate -> viewModel.setEndDate(selectedDate) }
            )

            ListItem(
                primaryText = "Сумма",
                itemBackgroundColor = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.height(55.dp),
                trailingText = totalExpenses,
                trailingIcon = null
            )
            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.tertiary)

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            } else if (error != null) {
                Text(
                    text = "Ошибка загрузки!",
                    color = MaterialTheme.colorScheme.error,
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
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                LazyColumn() {
                    items(transactions) { transaction ->
                        ListItem(
                            onClick = { /*TODO*/ },
                            primaryText = "${transaction.category.name}",
                            secondaryText ="${transaction.comment}" ,
                            leadingIconOrEmoji = "${transaction.category.emoji.orEmpty()}",
                            trailingText = formatAmountSimple(amountString = transaction.amount, currencyCode = transaction.account.currency),
                            secondTrailingText = "${transaction.createdAt.toFormattedTime()}",
                            modifier = Modifier.fillMaxWidth()
                                .height(69.dp)
                        )
                        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.tertiary)
                    }
                }
            }
        }
    }
}