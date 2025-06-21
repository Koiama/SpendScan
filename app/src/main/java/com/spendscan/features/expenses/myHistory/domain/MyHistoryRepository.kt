package com.spendscan.features.expenses.myHistory.domain

import com.spendscan.features.expenses.myHistory.data.models.TransactionDto

interface MyHistoryRepository {
    // Метод для получения транзакций за период
    suspend fun getTransactionsByPeriod(
        accountId: String,
        startDate: String?,
        endDate: String?
    ) : List<TransactionDto>
}