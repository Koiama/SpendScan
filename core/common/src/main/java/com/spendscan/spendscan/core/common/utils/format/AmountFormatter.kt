package com.spendscan.spendscan.core.common.utils.format

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

fun formatMoney(amount: Double, currencySymbol: String = "", withSpaces: Boolean = true): String {
    val symbols = DecimalFormatSymbols(Locale.getDefault()).apply {
        groupingSeparator = ' '
        decimalSeparator = '.'
    }

    val formatter = DecimalFormat("#,##0.00", symbols).apply {
        isGroupingUsed = true
    }

    val formatted = formatter.format(amount)
    return if (withSpaces) {
        "$formatted $currencySymbol".trim()
    } else {
        "${formatted.replace(" ", "")} $currencySymbol".trim()
    }
}
