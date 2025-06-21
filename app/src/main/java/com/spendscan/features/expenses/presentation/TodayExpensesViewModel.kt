package com.spendscan.features.expenses.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spendscan.features.expenses.myHistory.data.models.TransactionDto
import com.spendscan.features.expenses.myHistory.domain.MyHistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.LocalTime

// TodayExpensesViewModel: ViewModel для отображения расходов за текущий день.
// Он загружает транзакции, фильтрует их по типу "расход" и за сегодняшний день,
// а также рассчитывает общую сумму расходов за этот период.
class TodayExpensesViewModel(
    private val repository: MyHistoryRepository,
    private val accountId: String // accountId приходит через конструктор
) : ViewModel() {

    private val _transactions = MutableStateFlow<List<TransactionDto>>(emptyList())
    val transactions: StateFlow<List<TransactionDto>> = _transactions.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _totalExpenses = MutableStateFlow("") // Общая сумма расходов за сегодня с валютой
    val totalExpenses: StateFlow<String> = _totalExpenses.asStateFlow()

    init {
        loadTransactionsForToday()
    }

    private fun loadTransactionsForToday() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                // Получаем текущую дату
                val today = LocalDate.now()
                // Начало сегодняшнего дня (в миллисекундах UTC)
                val startOfTodayMillis = today.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
                // Конец сегодняшнего дня (в миллисекундах UTC)
                val endOfTodayMillis = today.atTime(LocalTime.MAX).atZone(ZoneOffset.UTC).toInstant().toEpochMilli()

                // Форматируем даты для запроса
                val formattedStartDate = Instant.ofEpochMilli(startOfTodayMillis)
                    .atZone(ZoneOffset.UTC)
                    .toLocalDate()
                    .format(DateTimeFormatter.ISO_LOCAL_DATE)

                val formattedEndDate = Instant.ofEpochMilli(endOfTodayMillis)
                    .atZone(ZoneOffset.UTC)
                    .toLocalDate()
                    .atTime(LocalTime.MAX)
                    .format(DateTimeFormatter.ISO_LOCAL_DATE)

                Log.d("SpendScanApp", "Запрос транзакций для Account ID: $accountId, Start Date: $formattedStartDate, End Date: $formattedEndDate (Today Expenses)")

                val fetchedTransactions =
                    repository.getTransactionsByPeriod(accountId, formattedStartDate, formattedEndDate)

                val onlyExpenses = fetchedTransactions
                    .filter { transactionDto ->
                        !(transactionDto.category.isIncome ?: true)
                    }
                    .sortedByDescending { transactionDto ->
                        try {
                            Instant.parse(transactionDto.createdAt)
                        } catch (e: Exception) {
                            Log.e("SpendScanApp", "ОШИБКА СОРТИРОВКИ: Не удалось разобрать createdAt '${transactionDto.createdAt}'. Используется Instant.MIN.", e)
                            Instant.MIN
                        }
                    }

                _transactions.value = onlyExpenses

                // Расчет общей суммы расходов с учетом валюты
                var currentTotalAmount = BigDecimal.ZERO
                var currency: String? = null

                _transactions.value.forEach { transactionDto ->
                    try {
                        currentTotalAmount = currentTotalAmount.add(BigDecimal(transactionDto.amount).abs())
                        if (currency == null) {
                            currency = transactionDto.account.currency
                        } else if (currency != transactionDto.account.currency) {
                            Log.w("SpendScanApp", "Транзакции расходов имеют разные валюты за сегодня. Сумма может быть неточной.")
                        }
                    } catch (e: NumberFormatException) {
                        Log.e("SpendScanApp", "Ошибка при разборе суммы '${transactionDto.amount}'. Транзакция пропущена.", e)
                    }
                }

                _totalExpenses.value = "$currentTotalAmount ${currency ?: ""}"
                Log.d("SpendScanApp", "TodayExpensesViewModel: Транзакции расходов за сегодня загружены. Количество: ${_transactions.value.size}")
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки расходов за сегодня: ${e.message ?: "Неизвестная ошибка"}"
                Log.e("SpendScanApp", "TodayExpensesViewModel: Ошибка при загрузке расходов за сегодня: ${e.message}", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}