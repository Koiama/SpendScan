package com.spendscan.features.expenses.domain.models

import com.spendscan.core.domain.models.Transaction

// Класс для возврата данных из UseCase
data class TodayExpensesData(
    val transactions: List<Transaction>,
    val totalAmount: Double,
    val currency: String
)