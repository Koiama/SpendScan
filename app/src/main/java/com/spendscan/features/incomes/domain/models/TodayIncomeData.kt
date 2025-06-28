package com.spendscan.features.incomes.domain.models

import com.spendscan.core.domain.models.Transaction

// Класс для возврата данных из UseCase
data class TodayIncomeData(
    val transactions: List<Transaction>,
    val totalAmount: Double,
    val currency: String
)