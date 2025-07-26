package com.spendscan.spendscan.feature.edit_balance.domain.usecase

import com.spendscan.spendscan.core.common.utils.Result
import com.spendscan.spendscan.core.domain.models.transaction.Transaction
import com.spendscan.spendscan.core.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

class GetTransactionsForLastMonthUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(accountId: Long): Result<List<Transaction>> {
        return try {
            val today = LocalDate.now()
            val startOfLastMonth = today.minusMonths(1).withDayOfMonth(1)
            val endOfLastMonth = YearMonth.from(today.minusMonths(1)).atEndOfMonth()

            val startDate = startOfLastMonth
            val endDate = endOfLastMonth

            val startDateForQuery = today.minusDays(30)
            val endDateForQuery = today

            val transactions = transactionRepository.getTransactions(
                startDate = startDateForQuery,
                endDate = endDateForQuery,
                accountId = accountId
            ).firstOrNull()

            if (transactions == null) {
                Result.Success(emptyList())
            } else {
                Result.Success(transactions)
            }
        } catch (e: Exception) {
            Result.Exception(e)
        }
    }
}