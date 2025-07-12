package com.spendscan.spendscan.core.data.repository

import android.util.Log
import retrofit2.HttpException
import com.spendscan.spendscan.core.common.utils.Result
import com.spendscan.spendscan.core.data.api.TransactionApi
import com.spendscan.spendscan.core.data.mapper.transaction.toDomain
import com.spendscan.spendscan.core.data.models.transaction.CreateTransactionRequest
import com.spendscan.spendscan.core.data.models.transaction.UpdateTransactionRequest
import com.spendscan.spendscan.core.domain.models.transaction.Transaction
import com.spendscan.spendscan.core.domain.repository.TransactionRepository
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * Реализация репозитория для работы с транзакциями.
 * @param api Сетевой клиент для работы с API транзакций
 */
class TransactionRepositoryImpl @Inject constructor(
    private val api: TransactionApi
) : TransactionRepository {

    override suspend fun getTransactions(
        startDate: LocalDate,
        endDate: LocalDate,
        accountId: Long
    ): Result<List<Transaction>> {
        return try {
            val startDateStr = startDate.toString()
            val endDateStr = endDate.toString()
            val response = api.getTransactionsByAccountAndPeriod(
                accountId = accountId,
                startDate = startDateStr,
                endDate = endDateStr
            )
            Result.Success(response.body()?.map { it.toDomain() } ?: emptyList())
        } catch (e: HttpException) {
            Result.Error(e.code(), e.message())
        } catch (e: Throwable) {
            Result.Exception(e)
        }
    }

    override suspend fun createTransaction(
        accountId: Long,
        categoryId: Long,
        amount: String,
        transactionDate: LocalDateTime,
        comment: String
    ): Result<Unit> {
        return try {
            api.createTransaction(
                CreateTransactionRequest.create(
                    accountId,
                    categoryId,
                    amount,
                    transactionDate,
                    comment
                )
            )
            Result.Success(Unit)
        } catch (e: HttpException) {
            Log.d("DEBUG", "createTransaction: $e")
            Result.Error(e.code(), e.message())
        } catch (e: Throwable) {
            Log.d("DEBUG", "createTransaction: $e")
            Result.Exception(e)
        }
    }

    override suspend fun updateTransaction(
        transactionId: Long,
        accountId: Long?,
        categoryId: Long?,
        amount: String?,
        transactionDate: LocalDateTime?,
        comment: String?
    ): Result<Unit> {
        return try {
            api.updateTransaction(
                id = transactionId,
                updateTransactionRequest = UpdateTransactionRequest.create(
                    accountId,
                    categoryId,
                    amount,
                    transactionDate,
                    comment
                )
            )
            Result.Success(Unit)
        } catch (e: HttpException) {
            Result.Error(e.code(), e.message())
        } catch (e: Throwable) {
            Result.Exception(e)
        }
    }

    override suspend fun deleteTransaction(transactionId: Long): Result<Unit> {
        return try {
            api.deleteTransaction(id = transactionId)
            Result.Success(Unit)
        } catch (e: HttpException) {
            Result.Error(e.code(), e.message())
        } catch (e: Throwable) {
            Result.Exception(e)
        }
    }

    override suspend fun getTransactionById(transactionId: Long): Result<Transaction?> {
        return try {
            val resp = api.getTransactionById(id = transactionId)
            Result.Success(resp.body()?.toDomain())
        } catch (e: HttpException) {
            Result.Error(e.code(), e.message())
        } catch (e: Throwable) {
            Result.Exception(e)
        }
    }

}
