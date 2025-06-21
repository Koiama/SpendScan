package com.spendscan.features.incomes.myHistory.presentation

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
import com.spendscan.features.incomes.incomesList
import com.spendscan.ui.components.ListItem
import com.spendscan.ui.components.TopBar
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.spendscan.features.incomes.myHistory.data.RetrofitClient
import com.spendscan.features.incomes.myHistory.data.models.formatAmountSimple
import com.spendscan.features.incomes.myHistory.data.models.toFormattedTime

//Только история расходов, ручка period

@Composable
fun MyHistoryScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: MyHistoryViewModel = viewModel(
        factory = MyHistoryViewModelFactory(RetrofitClient.myHistoryRepository)
    )
) {
    val transactions by viewModel.transactions.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(key1 = Unit) {
        val userAccountId = "1" // Пример ID из Swagger
        viewModel.loadTransactions(userAccountId)
    }

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
    ) { innearPadding ->
        Column(modifier = Modifier.padding(top = innearPadding.calculateTopPadding())) {
            ListItem(
                primaryText = "Начало",
                itemBackgroundColor = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.height(55.dp),
                trailingText = "Февраль 2025",
                trailingIcon = null
            )
            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.tertiary)
            ListItem(
                primaryText = "Конец",
                itemBackgroundColor = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.height(55.dp),
                trailingText = "23:41",
                trailingIcon = null
            )
            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.tertiary)
            ListItem(
                primaryText = "Сумма",
                itemBackgroundColor = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.height(55.dp),
                trailingText = "125 868 ₽",
                trailingIcon = null
            )
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