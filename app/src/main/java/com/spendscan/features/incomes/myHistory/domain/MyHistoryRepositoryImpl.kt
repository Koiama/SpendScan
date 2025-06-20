package com.spendscan.features.incomes.myHistory.domain

import com.spendscan.features.incomes.myHistory.data.api.TransactionApiService
import com.spendscan.features.incomes.myHistory.data.models.TransactionDto

class MyHistoryRepositoryImpl(private val apiService: TransactionApiService) : MyHistoryRepository {
    override suspend fun getTransactionsByPeriod(
        accountId: String,
        startDate: String?,
        endDate: String?
    ): List<TransactionDto> {
        return apiService.getTransactionsByPeriod(accountId, startDate, endDate)
    }
}