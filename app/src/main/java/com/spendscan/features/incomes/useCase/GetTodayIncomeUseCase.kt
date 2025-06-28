package com.spendscan.features.incomes.useCase

import android.util.Log
import com.spendscan.core.domain.repository.TransactionRepository
import com.spendscan.core.common.Result
import com.spendscan.features.incomes.domain.models.TodayIncomeData
import java.time.LocalDate
import kotlin.math.abs


class GetTodayIncomeUseCase(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(accountId: Int): Result<TodayIncomeData> {
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

                    val onlyIncome = fetchedTransactions
                        .filter { transaction ->
                            transaction.category.isIncome
                        }
                        .sortedByDescending { transaction ->
                            transaction.date
                        }

                    var currentTotalAmount: Double = 0.0
                    var currency: String? = null

                    onlyIncome.forEach { transaction ->
                        currentTotalAmount += abs(transaction.amount)
                        if (currency == null) {
                            currency = transaction.account.currency
                        } else if (currency != transaction.account.currency) {
                            Log.w("SpendScanApp", "Транзакции доходов имеют разные валюты за сегодня. Сумма может быть неточной.")
                        }
                    }

                    if (currency == null && onlyIncome.isNotEmpty()) {
                        currency = onlyIncome.first().account.currency
                    } else if (currency == null) {
                        currency = ""
                    }

                    Result.Success(TodayIncomeData(onlyIncome, currentTotalAmount, currency ?: ""))
                }
                is Result.Error -> {
                    resultFromRepo
                }
                is Result.Exception -> {
                    resultFromRepo
                }
            }

        } catch (e: Exception) {
            Log.e("SpendScanApp", "UseCase: Ошибка при загрузке или обработке доходов за сегодня: ${e.message}", e)
            Result.Exception(e)
        }
    }
}