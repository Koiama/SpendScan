package com.spendscan.spendscan.feature.balance.ui.presentation.components


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.spendscan.expenses_graph.ExpensesGraph
import com.spendscan.expenses_graph.ExpensesGraphElement
import com.spendscan.feature.balance.R
import com.spendscan.spendscan.core.domain.models.transaction.Transaction
import com.spendscan.spendscan.core.ui.components.ErrorText
import com.spendscan.spendscan.core.ui.components.ListItem
import com.spendscan.spendscan.core.ui.components.Loading
import com.spendscan.spendscan.feature.balance.ui.viewmodel.contract.BalanceState
import java.time.LocalDate
import kotlin.math.abs

@Composable
fun BalanceScreen(
    uiState: BalanceState,
    modifier: Modifier = Modifier,
) {
    when {
        uiState.isLoading -> Loading(modifier = Modifier.fillMaxSize())
        !uiState.error.isNullOrBlank() -> ErrorText(
            errorMessage = uiState.error,
            modifier = modifier.fillMaxSize()
        )

        else -> {
            Column(
                modifier = modifier
            ) {
                ListItem(
                    content = "Баланс",
                    trailingText = uiState.amount,
                    trailingIcon = ImageVector.vectorResource(R.drawable.forward_arrow_icon),
                    emojiBackgroundColor = Color.White,
                    leadEmoji = "\uD83D\uDCB0",
                    isHighlighted = true,
                    onItemClick = {},
                    modifier = Modifier.height(56.dp)
                )
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                ListItem(
                    content = "Валюта",
                    trailingText = uiState.currency.symbol,
                    trailingIcon = ImageVector.vectorResource(R.drawable.forward_arrow_icon),
                    isHighlighted = true,
                    onItemClick = {},
                    modifier = Modifier.height(56.dp)
                )


                if (uiState.isLoading && uiState.transactions.isEmpty() && uiState.amount != "0.00") {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Loading()
                    }
                } else {
                    val graphElements = remember(uiState.transactions) {
                        mapTransactionsToChartEntries(uiState.transactions)
                    }

                    if (graphElements.isNotEmpty()) {
                        ExpensesGraph(
                            elements = graphElements,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(top = 16.dp, bottom = 16.dp)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            // Если ошибка при загрузке транзакций, но данные счета есть
                            if (!uiState.error.isNullOrBlank() && uiState.amount != "0.00") {
                                Text(text = "")
                            } else {
                                Text(text = "")
                            }
                        }
                    }
                }
                // Показываем сообщение об ошибке под графиком, если она есть и данные счета уже загружены
                if (!uiState.error.isNullOrBlank() && uiState.amount != "0.00" && (uiState.isLoading || uiState.transactions.isEmpty())) {
                    Text(
                        text = "",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

private fun mapTransactionsToChartEntries(transactions: List<Transaction>): List<ExpensesGraphElement> {
    val groupedByDate = transactions.groupBy {
        it.date.toLocalDate()
    }

    val today = LocalDate.now()
    val numberOfDays = 10

    val elements = (0 until numberOfDays).map { offset ->
        val dateForDay = today.minusDays(((numberOfDays - 1) - offset).toLong())

        val dailyTransactions = groupedByDate[dateForDay].orEmpty()

        val expensesSum = dailyTransactions
            .filter { it.category != null && !it.category.isIncome }
            .sumOf { it.amount }

        val incomeSum = dailyTransactions
            .filter { it.category != null && it.category.isIncome }
            .sumOf { it.amount }

        val netAmount = expensesSum - incomeSum
        val isPositive = netAmount <= 0.0

        ExpensesGraphElement(
            date = dateForDay,
            amount = abs(netAmount).toFloat(),
            isPositive = isPositive
        )
    }
    return elements
}