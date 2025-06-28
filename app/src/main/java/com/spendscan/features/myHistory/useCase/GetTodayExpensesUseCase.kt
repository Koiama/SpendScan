package com.spendscan.features.myHistory.useCase


import android.util.Log
import com.spendscan.core.domain.models.Transaction
import com.spendscan.core.domain.repository.TransactionRepository
import com.spendscan.core.common.Result
import com.spendscan.core.common.map
import com.spendscan.features.myHistory.models.HistoryData
import java.time.LocalDate
import kotlin.math.abs


/**
 * UseCase для получения, фильтрации и расчета транзакций по типу (доходы/расходы) за указанный период.
 * Инкапсулирует бизнес-логику для получения транзакций истории.
 */
class GetTransactionsByTypeUseCase(
    private val transactionRepository: TransactionRepository
) {

    suspend operator fun invoke(
        accountId: Int,
        startDate: LocalDate,
        endDate: LocalDate,
        isIncome: Boolean? = null
    ): Result<HistoryData> {
        return transactionRepository.getTransactions(
            startDate = startDate,
            endDate = endDate,
            accountId = accountId
        ).map { transactions ->
            val filteredTransactions = if (isIncome != null) {
                transactions.filter { it.category.isIncome == isIncome }
            } else {
                transactions
            }

            val sortedTransactions = filteredTransactions.sortedByDescending { it.date }

            val amount = calculateAmount(filteredTransactions)

            val currency = filteredTransactions.firstOrNull()?.account?.currency ?: ""

            Log.d("SpendScanApp", "UseCase: Transactions calculated. Count: ${sortedTransactions.size}, Amount: $amount $currency")

            HistoryData(
                expenses = sortedTransactions,
                amount = amount,
                currency = currency
            )
        }
    }

    /**
     * Вычисляет общую сумму транзакций.
     */
    private fun calculateAmount(
        transactions: List<Transaction>
    ): Double {
        return transactions.sumOf { abs(it.amount) }
    }
}

