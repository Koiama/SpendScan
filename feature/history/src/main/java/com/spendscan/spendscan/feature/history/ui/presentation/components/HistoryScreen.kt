package com.spendscan.spendscan.feature.history.ui.presentation.components


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.spendscan.spendscan.core.common.utils.format.formatMoney
import com.spendscan.spendscan.core.ui.components.DateSelector
import com.spendscan.spendscan.core.ui.components.EmptyState
import com.spendscan.spendscan.core.ui.components.ErrorText
import com.spendscan.spendscan.core.ui.components.ListItem
import com.spendscan.spendscan.core.ui.components.Loading
import com.spendscan.spendscan.core.ui.components.utils.formatForDisplay
import com.spendscan.spendscan.feature.history.ui.viewmodel.contract.HistoryAction
import com.spendscan.spendscan.feature.history.ui.viewmodel.contract.HistoryAction.ChangeDatePickerVisibility
import com.spendscan.spendscan.feature.history.ui.viewmodel.contract.HistoryState

@Composable
fun HistoryScreen(
    uiState: HistoryState,
    onAction: (HistoryAction) -> Unit,
    onItemClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        uiState.isLoading -> Loading(modifier = modifier)
        uiState.errorMessage != null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                ErrorText(
                    errorMessage = uiState.errorMessage,
                    modifier = Modifier
                )
            }
        }

        else -> {
            Column(
                modifier = modifier
            ) {
                ListItem(
                    modifier = Modifier.height(56.dp).fillMaxWidth(),
                    isHighlighted = true,
                    content = "Начало",
                    onItemClick = { onAction(ChangeDatePickerVisibility) },
                    trailingText = uiState.dateStart.formatForDisplay(),
                )
                if (uiState.isDatePickerShown) {
                    DateSelector(
                        date = uiState.dateEnd,
                        onDismiss = { onAction(ChangeDatePickerVisibility) },
                        onDateSelected = { newDate ->
                            onAction(HistoryAction.UpdateDateEnd(newDate))
                        }
                    )
                }


                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                ListItem(
                    modifier = Modifier.height(56.dp).fillMaxWidth(),
                    isHighlighted = true,
                    content = "Конец",
                    onItemClick = { onAction(ChangeDatePickerVisibility) },
                    trailingText = uiState.dateEnd.formatForDisplay(),
                )
                if (uiState.isDatePickerShown) {
                    DateSelector(
                        date = uiState.dateEnd,
                        onDismiss = { onAction(ChangeDatePickerVisibility) },
                        onDateSelected = { newDate ->
                            onAction(HistoryAction.UpdateDateEnd(newDate))
                        }
                    )
                }

                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                ListItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    content = "Сумма",
                    isHighlighted = true,
                    trailingText = formatMoney(uiState.amount, uiState.currency.symbol)
                )

                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                if (uiState.transactions.isEmpty()) {
                    EmptyState(modifier = Modifier.fillMaxSize())
                } else {
                    LazyColumn {
                        items(uiState.transactions, key = { it.id }) { transaction ->
                            TransactionItem(
                                transaction,
                                onClick = {onItemClick(transaction.id)},
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(70.dp)
                            )
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.outlineVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
