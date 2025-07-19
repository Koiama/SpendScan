package com.spendscan.spendscan.core.domain.repository

import com.spendscan.spendscan.core.common.utils.Result
import com.spendscan.spendscan.core.domain.models.transaction.Transaction
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Репозиторий для управления операциями с транзакциями
 */
interface TransactionRepository {

    fun getTransactions(
        startDate: LocalDate,
        endDate: LocalDate,
        accountId: Long
    ): Flow<List<Transaction>>

    suspend fun createTransaction(
        accountId: Long,
        categoryId: Long,
        amount: String,
        transactionDate: LocalDateTime,
        comment: String,
    ): Result<Unit>

    @Suppress("LongParameterList")
    suspend fun updateTransaction(
        transactionId: Long,
        accountId: Long?,
        categoryId: Long?,
        amount: String?,
        transactionDate: LocalDateTime?,
        comment: String?,
    ): Result<Unit>

    suspend fun deleteTransaction(
        transactionId: Long
    ): Result<Unit>

    suspend fun getTransactionById(
        transactionId: Long
    ): Result<Transaction?>

    suspend fun syncTransactions(): Result<Unit>
}