package com.spendscan.spendscan.feature.history.domain.usecase

import com.spendscan.spendscan.core.common.utils.Result
import com.spendscan.spendscan.core.domain.models.account.Currency
import com.spendscan.spendscan.core.domain.models.transaction.Transaction
import com.spendscan.spendscan.core.domain.models.transaction.TransactionType
import com.spendscan.spendscan.core.domain.repository.TransactionRepository
import com.spendscan.spendscan.feature.history.domain.models.HistoryData
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDate
import javax.inject.Inject

/**
 * Usecase получения истории транзакций с фильтрацией по типу операций
 */
class GetHistoryUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    /**
     * Параметры:
     * @param startDate Начальная дата периода
     * @param endDate Конечная дата периода
     * @param accountId Идентификатор банковского счёта
     * @param transactionType Тип операций для фильтрации:
     *   - `TransactionType.INCOME`: только доходы
     *   - `TransactionType.EXPENSE`: только расходы
     */
    suspend operator fun invoke(
        startDate: LocalDate,
        endDate: LocalDate,
        accountId: Long,
        transactionType: TransactionType
    ): Result<HistoryData> {
        return try {
            val transactions = transactionRepository.getTransactions(
                startDate = startDate,
                endDate = endDate,
                accountId = accountId
            ).firstOrNull()

            if (transactions == null) {
                Result.Success(HistoryData(emptyList(), 0.0, Currency.RUB))
            } else {
                val filtered = when (transactionType) {
                    TransactionType.INCOME -> transactions.filter { it.category.isIncome }
                    TransactionType.EXPENSE -> transactions.filter { !it.category.isIncome }
                }
                val sorted: List<Transaction> = filtered.sortedByDescending { it.date }
                val amount = calculateAmount(filtered)
                val currency = filtered.firstOrNull()?.account?.currency ?: Currency.RUB

                val historyData = HistoryData(
                    transactions = sorted,
                    amount = amount,
                    currency = currency
                )
                Result.Success(historyData)
            }
        } catch (e: Exception) {
            Result.Error(4, "Не удалось получить историю транзакций")
        }
    }

    private fun calculateAmount(
        transactions: List<Transaction>
    ): Double {
        return transactions.sumOf { it.amount }
    }
}
