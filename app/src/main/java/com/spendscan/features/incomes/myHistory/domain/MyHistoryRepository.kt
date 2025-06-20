package com.spendscan.features.incomes.myHistory.domain

import com.spendscan.features.incomes.myHistory.data.models.TransactionDto

interface MyHistoryRepository {
    // Метод для получения транзакций за период
    suspend fun getTransactionsByPeriod(
        accountId: String,
        startDate: String?,
        endDate: String?
    ) : List<TransactionDto>
}