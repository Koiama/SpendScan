package com.spendscan.spendscan.feature.income.ui.presentation.components

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
import com.spendscan.feature.income.R


/**
 * Компонент для отображения списка доходов
 * @param income Список доходов
 * @param modifier Модификатор для кастомизации
 */
@Composable
fun IncomeList(
    income: List<Transaction>,
    onIncomeClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (income.isEmpty()) {
        EmptyState(modifier = modifier.fillMaxSize())
    } else {
        LazyColumn(
            modifier = modifier
        ) {
            items(
                income,
                key = { it.id }
            ) { income ->
                ListItem(
                    content = income.category.name,
                    leadEmoji = income.category.emoji,
                    trailingText = formatMoney(income.amount, income.account.currency.symbol),
                    trailingIcon = ImageVector.vectorResource(R.drawable.arrow_forward_icon),
                    comment = if (income.comment.isNullOrBlank()) null else income.comment,
                    onItemClick = { onIncomeClick(income.id) },
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
