package com.spendscan.core.data.repository

import com.spendscan.core.data.api.TransactionApiService
import com.spendscan.core.data.mapper.toDomain
import com.spendscan.core.domain.models.Transaction
import com.spendscan.core.domain.repository.TransactionRepository
import retrofit2.HttpException
import java.time.LocalDate
import com.spendscan.core.common.Result

/**
 * Реализация репозитория для работы с транзакциями.
 */

class TransactionRepositoryImpl(private val apiService: TransactionApiService) :
    TransactionRepository {

    override suspend fun getTransactions(
        startDate: LocalDate,
        endDate: LocalDate,
        accountId: Int
    ): Result<List<Transaction>> {
        return try {
            val startDateStr = startDate.toString()
            val endDateStr = endDate.toString()
            val response = apiService.getTransactionsByPeriod(
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
}