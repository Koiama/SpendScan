package com.spendscan.spendscan.feature.expenses.domain.usecase

import com.spendscan.spendscan.core.common.utils.Result
import com.spendscan.spendscan.core.common.utils.map
import com.spendscan.spendscan.core.domain.models.account.Currency
import com.spendscan.spendscan.core.domain.models.transaction.Transaction
import com.spendscan.spendscan.core.domain.repository.TransactionRepository
import com.spendscan.spendscan.feature.expenses.domain.models.ExpenseData
import java.time.LocalDate
import javax.inject.Inject

/**
 * Сценарий получения данных о расходах за текущий день.
 *
 */
class GetExpensesUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
){
    /**
     *  * @param accountId Идентификатор счёта у которого получаем транзакции
     */
    suspend operator fun invoke(
        accountId: Long
    ): Result<ExpenseData> {
        return transactionRepository.getTransactions(
            startDate = LocalDate.now(),
            endDate = LocalDate.now(),
            accountId = accountId
        ).map { transactions ->
            val filtered = transactions.filter { !it.category.isIncome }

            val sorted: List<Transaction> = filtered.sortedByDescending { it.date }

            val amount = calculateAmount(filtered)

            val currency = filtered.firstOrNull()?.account?.currency ?: Currency.RUB

            ExpenseData(
                expenses = sorted,
                amount = amount,
                currency = currency
            )
        }
    }

    private fun calculateAmount(
        transactions: List<Transaction>
    ): Double = transactions.sumOf { it.amount }
}
