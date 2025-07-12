package com.spendscan.spendscan.feature.expenses.ui.presentation.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.spendscan.spendscan.core.common.utils.format.formatMoney
import com.spendscan.spendscan.core.domain.models.transaction.Transaction
import com.spendscan.spendscan.core.ui.components.EmptyState
import com.spendscan.spendscan.core.ui.components.ListItem
import com.spendscan.feature.expenses.R

/**
 * Компонент для отображения списка расходов
 * @param expenses Список расходов для отображения
 * @param modifier Модификатор для доп. кастома
 */
@Composable
fun ExpensesList(
    expenses: List<Transaction>,
    onExpenseClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (expenses.isEmpty()) {
        EmptyState(modifier = modifier.fillMaxSize())
    } else {
        LazyColumn(
            modifier = modifier
        ) {
            items(
                expenses,
                key = { it.id }
            ) { expense ->
                ListItem(
                    content = expense.category.name,
                    leadEmoji = expense.category.emoji,
                    trailingText = formatMoney(expense.amount, expense.account.currency.symbol),
                    trailingIcon = ImageVector.vectorResource(R.drawable.arrow_forward_icon),
                    comment = if (expense.comment.isNullOrBlank()) null else expense.comment,
                    onItemClick = { onExpenseClick(expense.id) },
                    modifier = Modifier.height(70.dp)
                )
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
        }
    }
}
