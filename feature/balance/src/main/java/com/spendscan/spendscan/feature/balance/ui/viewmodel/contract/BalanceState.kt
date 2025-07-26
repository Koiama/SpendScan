package com.spendscan.spendscan.feature.balance.ui.viewmodel.contract

import com.spendscan.spendscan.core.domain.models.account.Currency
import com.spendscan.spendscan.core.domain.models.transaction.Transaction

/**
 * Состояния для экрана отображения баланса.
 */
data class BalanceState(
    val isLoading: Boolean = false,
    val amount: String = "0.00",
    val currency: Currency = Currency.RUB,
    val accountName: String = "",
    val error: String? = null,
    val accountId: Long = 0L,
    val transactions: List<Transaction> = emptyList()
)