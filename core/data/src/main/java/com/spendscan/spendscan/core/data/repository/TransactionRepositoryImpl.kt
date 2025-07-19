package com.spendscan.spendscan.core.data.repository

import android.util.Log
import retrofit2.HttpException
import com.spendscan.spendscan.core.common.utils.Result
import com.spendscan.spendscan.core.data.api.TransactionApi
import com.spendscan.spendscan.core.data.local.model.TransactionDao
import com.spendscan.spendscan.core.data.mapper.transaction.toDomain
import com.spendscan.spendscan.core.data.mapper.transaction.toEntity
import com.spendscan.spendscan.core.data.models.transaction.CreateTransactionRequest
import com.spendscan.spendscan.core.data.models.transaction.UpdateTransactionRequest
import com.spendscan.spendscan.core.domain.models.account.AccountBrief
import com.spendscan.spendscan.core.domain.models.account.Currency
import com.spendscan.spendscan.core.domain.models.transaction.Category
import com.spendscan.spendscan.core.domain.models.transaction.Transaction
import com.spendscan.spendscan.core.domain.repository.TransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * Реализация репозитория для работы с транзакциями.
 */
class TransactionRepositoryImpl @Inject constructor(
    private val api: TransactionApi,
    private val transactionDao: TransactionDao
) : TransactionRepository {
    override fun getTransactions(
        startDate: LocalDate,
        endDate: LocalDate,
        accountId: Long
    ): Flow<List<Transaction>> {
        val startDateTime = startDate.atStartOfDay() // Начало дня
        val endDateTime = endDate.atTime(23, 59, 59, 999999999)
        return transactionDao.getTransactionsByDateRange(startDateTime, endDateTime)
            .map { entities ->
                entities.filter { it.accountId == accountId }.map { it.toDomain() }
            }
            .flowOn(Dispatchers.IO)
    }
    override suspend fun createTransaction(
        accountId: Long,
        categoryId: Long,
        amount: String,
        transactionDate: LocalDateTime,
        comment: String
    ): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val newTransaction = Transaction(
                    id = 0L,
                    account = AccountBrief(
                        id = accountId,
                        name = "",
                        currency = Currency.RUB,
                        balance = 0.0
                    ), // Заглушки
                    category = Category(id = categoryId, name = "", emoji = "", isIncome = false), // Заглушки
                    amount = amount.toDouble(),
                    date = transactionDate,
                    comment = comment
                )

                transactionDao.insertTransaction(newTransaction.toEntity())

                // На этом этапе транзакция сохранена локально.
                // Отправка на сервер будет происходить при syncTransactions().
                Result.Success(Unit)
            } catch (e: Exception) {
                Log.e("TransactionRepository", "Error creating local transaction: $e")
                Result.Exception(e)
            }
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
        return withContext(Dispatchers.IO) {
            try {
                val existingTransaction = transactionDao.getAllTransactions()
                    .map { it.firstOrNull { tx -> tx.id == transactionId } }
                    .flowOn(Dispatchers.IO)
                    .take(1)
                    .singleOrNull()
                    ?.toDomain()

                if (existingTransaction != null) {
                    val updatedDomainTransaction = existingTransaction.copy(
                        account = accountId?.let { existingTransaction.account.copy(id = it) } ?: existingTransaction.account,
                        category = categoryId?.let { existingTransaction.category.copy(id = it) } ?: existingTransaction.category,
                        amount = amount?.toDouble() ?: existingTransaction.amount,
                        date = transactionDate ?: existingTransaction.date,
                        comment = comment ?: existingTransaction.comment,
                    )
                    transactionDao.updateTransaction(updatedDomainTransaction.toEntity())
                    Result.Success(Unit)
                } else {
                    Result.Error(404, "Transaction not found locally")
                }
            } catch (e: Exception) {
                Log.e("TransactionRepository", "Error updating local transaction: $e")
                Result.Exception(e)
            }
        }
    }

    override suspend fun deleteTransaction(transactionId: Long): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                transactionDao.deleteTransaction(transactionId)
                Result.Success(Unit)
            } catch (e: Exception) {
                Log.e("TransactionRepository", "Error deleting local transaction: $e")
                Result.Exception(e)
            }
        }
    }

    override suspend fun getTransactionById(transactionId: Long): Result<Transaction?> {
        return withContext(Dispatchers.IO) {
            try {
                val localTransaction = transactionDao.getAllTransactions()
                    .map { it.firstOrNull { tx -> tx.id == transactionId } }
                    .flowOn(Dispatchers.IO)
                    .take(1)
                    .singleOrNull()
                    ?.toDomain()

                if (localTransaction != null) {
                    Result.Success(localTransaction)
                } else {
                    val resp = api.getTransactionById(id = transactionId)
                    Result.Success(resp.body()?.toDomain())
                }
            } catch (e: HttpException) {
                Result.Error(e.code(), e.message())
            } catch (e: Throwable) {
                Result.Exception(e)
            }
        }
    }

    override suspend fun syncTransactions(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val remoteTransactionsResponse = api.getTransactionsByAccountAndPeriod(
                    accountId = 0L,
                    startDate = LocalDate.MIN.toString(),
                    endDate = LocalDate.MAX.toString()
                )

                if (remoteTransactionsResponse.isSuccessful) {
                    val remoteTransactionEntities = remoteTransactionsResponse.body()
                        ?.map { it.toEntity() } ?: emptyList()
                    transactionDao.insertAllTransactions(remoteTransactionEntities)

                    Result.Success(Unit)
                } else {
                    Result.Error(remoteTransactionsResponse.code(), remoteTransactionsResponse.message())
                }
            } catch (e: HttpException) {
                Result.Error(e.code(), e.message())
            } catch (e: Throwable) {
                Log.e("TransactionRepository", "Error during syncTransactions: $e")
                Result.Exception(e)
            }
        }
    }
}