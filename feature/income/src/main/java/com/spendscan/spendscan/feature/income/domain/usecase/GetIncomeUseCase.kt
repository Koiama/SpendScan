package com.spendscan.spendscan.feature.income.domain.usecase


import com.spendscan.spendscan.core.common.utils.Result
import com.spendscan.spendscan.core.common.utils.map
import com.spendscan.spendscan.core.domain.models.account.Currency
import com.spendscan.spendscan.core.domain.models.transaction.Transaction
import com.spendscan.spendscan.core.domain.repository.TransactionRepository
import com.spendscan.spendscan.feature.income.domain.models.IncomeData
import java.time.LocalDate
import javax.inject.Inject

/**
 * UseCase получения данных о доходах по счёту за текущий день.
 *
 * @param transactionRepository репозиторий для доступа к транзакциям
 */
class GetIncomeUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
){
    suspend operator fun invoke(
        accountId: Long
    ): Result<IncomeData> {
        return transactionRepository.getTransactions(
            startDate = LocalDate.now(),
            endDate = LocalDate.now(),
            accountId = accountId
        ).map { transactions ->
            val filtered = transactions.filter { it.category.isIncome }

            val sorted: List<Transaction> = filtered.sortedByDescending { it.date }

            val amount = calculateAmount(filtered)

            val currency = filtered.firstOrNull()?.account?.currency ?: Currency.RUB

            IncomeData(
                income = sorted,
                amount = amount,
                currency = currency
            )
        }
    }

    private fun calculateAmount(
        transactions: List<Transaction>
    ): Double = transactions.sumOf { it.amount }
}
