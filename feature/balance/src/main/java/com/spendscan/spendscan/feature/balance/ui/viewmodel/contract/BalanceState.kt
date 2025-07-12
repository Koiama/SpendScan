package com.spendscan.spendscan.feature.balance.ui.viewmodel.contract

import com.spendscan.spendscan.core.domain.models.account.Currency

/**
 * Состояния для экрана отображения баланса.
 */
data class BalanceState(
    val isLoading: Boolean = false,
    val amount: String = "0.00",
    val currency: Currency = Currency.RUB,
    val accountName: String = "",
    val error: String? = null
)
