package com.spendscan.spendscan.feature.history.ui.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.spendscan.spendscan.core.common.utils.format.formatMoney
import com.spendscan.spendscan.core.common.utils.format.formatTimeWithLeadingZero
import com.spendscan.spendscan.core.domain.models.transaction.Transaction
import com.spendscan.spendscan.core.ui.components.ListItem
import com.spendscan.feature.history.R

/**
 * Компонент для отображения транзакции
 */
@Composable
fun TransactionItem(
    transaction: Transaction,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ListItem(
        modifier = modifier,
        content = transaction.category.name,
        leadEmoji = transaction.category.emoji,
        trailingIcon = ImageVector.vectorResource(R.drawable.arrow_forward_icon),
        comment = if (transaction.comment.isNullOrEmpty()) null else transaction.comment,
        trailingText = formatMoney(
            transaction.amount,
            transaction.account.currency.symbol
        ),
        trailingSecondaryText = formatTimeWithLeadingZero(transaction.date),
        onItemClick = onClick
    )
}
