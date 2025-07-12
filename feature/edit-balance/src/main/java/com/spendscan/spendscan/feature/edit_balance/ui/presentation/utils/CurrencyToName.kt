package com.spendscan.spendscan.feature.edit_balance.ui.presentation.utils

import com.spendscan.spendscan.core.domain.models.account.Currency

fun Currency.toLongName() = when (this) {
    Currency.RUB -> "Российский рубль"
    Currency.USD -> "Американский доллар"
    Currency.EUR -> "Евро"
}