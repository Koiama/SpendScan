package com.spendscan.spendscan.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Suppress("LongParameterList")
@Composable
fun FinTrackerTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    currencySymbol: String? = null,
    isHighlighted: Boolean = true,
) {
    val backgroundModifier =
        Modifier.background(color = if (isHighlighted) MaterialTheme.colorScheme.secondary else Color.Transparent)
    Row(
        modifier = modifier
            .then(backgroundModifier)
            .padding(start = 16.dp, top = 8.dp, end = 30.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        title?.let {
            Text(title)
        }
        BasicTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            modifier = Modifier.weight(1f),
            keyboardOptions = keyboardOptions,
            textStyle = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.End),
            singleLine = true,
        )
        currencySymbol?.let {
            Text(" $currencySymbol")
        }
    }
}