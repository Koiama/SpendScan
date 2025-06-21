package com.spendscan.features.expenses.myHistory.presentation

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
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.LocalTime
import java.time.format.DateTimeParseException

class MyHistoryViewModel(
    private val repository: MyHistoryRepository,
    private val accountId: String // accountId приходит через конструктор
) : ViewModel() {

    private val _transactions = MutableStateFlow<List<TransactionDto>>(emptyList())
    val transactions: StateFlow<List<TransactionDto>> = _transactions.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _startDate = MutableStateFlow<Long?>(null)
    val startDate: StateFlow<Long?> = _startDate.asStateFlow()

    private val _endDate = MutableStateFlow<Long?>(null)
    val endDate: StateFlow<Long?> = _endDate.asStateFlow()

    private val _totalExpenses = MutableStateFlow(BigDecimal.ZERO)
    val totalExpenses: StateFlow<BigDecimal> = _totalExpenses.asStateFlow()

    init {
        loadTransactions()
    }

    fun setStartDate(millis: Long?) {
        _startDate.value = millis
        loadTransactions() // Перезагружаем транзакции при изменении начальной даты
    }

    fun setEndDate(millis: Long?) {
        _endDate.value = millis
        loadTransactions() // Перезагружаем транзакции при изменении конечной даты
    }

    fun loadTransactions() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {

                val formattedStartDate = _startDate.value?.let { millis ->
                    Instant.ofEpochMilli(millis)
                        .atZone(ZoneOffset.UTC)
                        .toLocalDate()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE)
                }

                val formattedEndDate = _endDate.value?.let { millis ->
                    Instant.ofEpochMilli(millis)
                        .atZone(ZoneOffset.UTC)
                        .toLocalDate()
                        .atTime(LocalTime.MAX)
                        .format(DateTimeFormatter.ISO_LOCAL_DATE)
                }

                Log.d("SpendScanApp", "Requesting transactions for Account ID: $accountId, Start Date: $formattedStartDate, End Date: $formattedEndDate")

                val fetchedTransactions =
                    repository.getTransactionsByPeriod(accountId, formattedStartDate, formattedEndDate)


                // СНАЧАЛА ФИЛЬТРУЕМ, ПОТОМ СОРТИРУЕМ И ОБНОВЛЯЕМ _transactions ---
                val onlyExpenses = fetchedTransactions
                    .filter { transactionDto ->
                        !(transactionDto.category.isIncome ?: true)
                    }
                    .sortedByDescending { transactionDto ->
                        try {
                            Instant.parse(transactionDto.createdAt)
                        } catch (e: DateTimeParseException) {
                            Log.e("SpendScanApp", "SORTING ERROR: Failed to parse createdAt '${transactionDto.createdAt}'. Defaulting to Instant.MIN.", e)
                            Instant.MIN
                        } catch (e: Exception) {
                            Log.e("SpendScanApp", "SORTING ERROR: Unexpected error parsing createdAt '${transactionDto.createdAt}'. Defaulting to Instant.MIN.", e)
                            Instant.MIN
                        }
                    }

                _transactions.value = onlyExpenses

                // --- РАСЧЕТ СУММЫ РАСХОДОВ ---
                val currentTotal = _transactions.value
                    .mapNotNull { transactionDto ->
                        try {
                            BigDecimal(transactionDto.amount).abs()
                        } catch (e: NumberFormatException) {
                            Log.e("SpendScanApp", "Error parsing amount '${transactionDto.amount}'. Skipping transaction from total calculation.", e)
                            null
                        }
                    }
                    .fold(BigDecimal.ZERO) { acc, amount -> acc.add(amount) }

                Log.d("SpendScanApp", "MyHistoryViewModel: Transactions loaded successfully. Count: ${_transactions.value.size}")
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки транзакций: ${e.message ?: "Неизвестная ошибка"}"
                Log.e("SpendScanApp", "MyHistoryViewModel: Error loading transactions: ${e.message}", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}