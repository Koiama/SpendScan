package com.spendscan.core.domain.repository

import com.spendscan.core.common.Result
import com.spendscan.core.domain.models.Transaction
import java.time.LocalDate

/**
 * Интерфейс репозитория для работы с транзакциями.
 * Определяет контракт для получения доменных моделей транзакций.
 */

interface  TransactionRepository {

    suspend fun getTransactions(
        startDate: LocalDate,
        endDate: LocalDate,
        accountId: Int
    ): Result<List<Transaction>>
}