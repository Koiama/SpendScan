package com.spendscan.spendscan.feature.income.ui.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.spendscan.spendscan.core.common.utils.format.formatMoney
import com.spendscan.spendscan.core.ui.components.ErrorText
import com.spendscan.spendscan.core.ui.components.ListItem
import com.spendscan.spendscan.core.ui.components.Loading
import com.spendscan.spendscan.feature.income.ui.viewmodel.contract.IncomeState

@Composable
fun IncomeScreen(
    uiState: IncomeState,
    onIncomeClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        when {
            uiState.isLoading -> {
                Loading(modifier = Modifier.fillMaxSize())
            }

            uiState.errorMessage != null -> {
                ErrorText(
                    errorMessage = uiState.errorMessage,
                    modifier = Modifier.fillMaxSize()
                )
            }

            else -> {
                Column(modifier = Modifier.fillMaxSize()) {
                    ListItem(
                        content = "Всего",
                        trailingText = formatMoney(uiState.amount, uiState.currency.symbol),
                        isHighlighted = true,
                        modifier = Modifier
                            .height(56.dp)
                    )
                    IncomeList(
                        income = uiState.incomes,
                        onIncomeClick = onIncomeClick
                    )
                }
            }
        }
    }
}
