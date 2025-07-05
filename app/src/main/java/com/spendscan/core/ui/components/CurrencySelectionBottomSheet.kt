package com.spendscan.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.spendscan.features.account.domain.models.Currency

@Composable
fun CurrencySelectionBottomSheet(
    currencies: List<Currency>,
    selectedCurrency: Currency,
    onCurrencySelected: (Currency) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Выберите валюту",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        currencies.forEach { currency ->
            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onCurrencySelected(currency) },
                primaryText = "${currency.name} ${currency.symbol}",
                itemBackgroundColor = if (currency == selectedCurrency) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}