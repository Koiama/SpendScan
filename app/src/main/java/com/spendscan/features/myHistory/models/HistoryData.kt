package com.spendscan.features.myHistory.models

import com.spendscan.core.domain.models.Transaction

/**
 * Доменная модель, агрегирующая данные о расходах для UI.
 * Упаковывает список транзакций, их общую сумму и валюту.
 */
data class HistoryData(
    val expenses: List<Transaction>,
    val amount: Double,
    val currency: String
)