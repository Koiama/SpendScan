package com.spendscan.features.expenses.useCase

import android.util.Log
import com.spendscan.core.domain.repository.TransactionRepository
import com.spendscan.core.common.Result
import com.spendscan.features.expenses.domain.models.TodayExpensesData
import java.time.LocalDate
import kotlin.math.abs


class GetTodayExpensesUseCase(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(accountId: Int): Result<TodayExpensesData> {
        return try {
            val today = LocalDate.now()

            val resultFromRepo = repository.getTransactions(
                startDate = today,
                endDate = today,
                accountId = accountId
            )

            when (resultFromRepo) {
                is Result.Success -> {
                    val fetchedTransactions = resultFromRepo.data

                    val onlyExpenses = fetchedTransactions
                        .filter { transaction -> !transaction.category.isIncome }
                        .sortedByDescending { transaction -> transaction.date }

                    var currentTotalAmount: Double = 0.0
                    var currency: String? = null

                    onlyExpenses.forEach { transaction ->
                        currentTotalAmount += abs(transaction.amount)
                        if (currency == null) {
                            currency = transaction.account.currency
                        } else if (currency != transaction.account.currency) {
                            Log.w("SpendScanApp", "Транзакции расходов имеют разные валюты за сегодня. Сумма может быть неточной.")
                        }
                    }

                    if (currency == null && onlyExpenses.isNotEmpty()) {
                        currency = onlyExpenses.first().account.currency
                    } else if (currency == null) {
                        currency = ""
                    }

                    Result.Success(TodayExpensesData(onlyExpenses, currentTotalAmount, currency ?: ""))
                }
                is Result.Error -> {
                    resultFromRepo
                }
                is Result.Exception -> {
                    resultFromRepo
                }
            }

        } catch (e: Exception) {
            Log.e("SpendScanApp", "UseCase: Ошибка при загрузке или обработке расходов за сегодня: ${e.message}", e)
            Result.Exception(e)
        }
    }
}