package com.spendscan.features.incomes.myHistory.data.models

import com.spendscan.features.account.domain.models.Currency
import java.text.NumberFormat
import java.util.Locale

fun formatAmountSimple(amountString: String, currencyCode: String): String {
    val amount = amountString.toDouble()

    val numberFormat = NumberFormat.getInstance(Locale.getDefault()).apply {
        isGroupingUsed = true // Включаем разделитель тысяч
        minimumFractionDigits = 0 // Убираем десятичные знаки, если они .00
        maximumFractionDigits = 2 // Оставляем до двух десятичных знаков, если есть
    }

    val formattedNumber = numberFormat.format(amount)
    val symbol = Currency.fromCode(currencyCode)?.symbol ?: currencyCode

    return "$formattedNumber $symbol"

}