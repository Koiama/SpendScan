package com.spendscan.spendscan.feature.balance.ui.presentation.components


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.spendscan.feature.balance.R
import com.spendscan.spendscan.core.ui.components.ErrorText
import com.spendscan.spendscan.core.ui.components.ListItem
import com.spendscan.spendscan.core.ui.components.Loading
import com.spendscan.spendscan.feature.balance.ui.viewmodel.contract.BalanceState

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
            }
        }
    }
}
