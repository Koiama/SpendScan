package com.spendscan.core.common

import com.spendscan.features.account.domain.models.Currency
import java.text.NumberFormat
import java.util.Locale

/**
 * Функция для форматирования суммы в нужном формате
 */


fun formatAmount(amount: Double, currencyCode: String): String {

    val numberFormat = NumberFormat.getInstance(Locale.getDefault()).apply {
        isGroupingUsed = true
        minimumFractionDigits = 0
        maximumFractionDigits = 2
    }

    val formattedNumber = numberFormat.format(amount)
    val symbol = Currency.Companion.fromCode(currencyCode)?.symbol ?: currencyCode

    return "$formattedNumber $symbol"

}