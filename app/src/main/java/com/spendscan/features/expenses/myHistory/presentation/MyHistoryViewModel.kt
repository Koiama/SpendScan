package com.spendscan.features.expenses.myHistory.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spendscan.features.expenses.myHistory.data.ConnectivityObserver
import com.spendscan.features.expenses.myHistory.data.NetworkStatus
import com.spendscan.features.expenses.myHistory.data.models.TransactionDto
import com.spendscan.features.expenses.myHistory.domain.MyHistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.IOException
import java.math.BigDecimal
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.LocalTime
import java.time.format.DateTimeParseException

class MyHistoryViewModel(
    private val repository: MyHistoryRepository,
    private val accountId: String,
    private val connectivityObserver: ConnectivityObserver
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

    private val _totalExpenses = MutableStateFlow("")
    val totalExpenses: StateFlow<String> = _totalExpenses.asStateFlow()

    private val _isOnline = MutableStateFlow(true) // <-- Состояние сети
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    init {
        // Начинаем отслеживать состояние сети
        connectivityObserver.observe().onEach { status ->
            _isOnline.value = status == NetworkStatus.Available
            if (!isOnline.value) {
                // Если нет сети, устанавливаем ошибку
                _error.value = "Нет подключения к интернету."
                _isLoading.value = false // Останавливаем индикатор загрузки
            } else if (_error.value == "Нет подключения к интернету." || _error.value?.contains("Failed to fetch transactions after") == true) {
                // Если сеть появилась и предыдущая ошибка была из-за сети/перезапросов, пробуем перезагрузить
                _error.value = null
                loadTransactions()
            }
        }.launchIn(viewModelScope)

        loadTransactions()
    }

    fun setStartDate(millis: Long?) {
        _startDate.value = millis
        loadTransactions()
    }

    fun setEndDate(millis: Long?) {
        _endDate.value = millis
        loadTransactions()
    }

    fun loadTransactions() {
        viewModelScope.launch {
            if (!_isOnline.value) { // Если нет сети, не пытаемся загружать
                _error.value = "Нет подключения к интернету. Данные не загружены."
                return@launch
            }

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

                val onlyExpenses = fetchedTransactions
                    .filter { transactionDto ->
                        !(transactionDto.category.isIncome ?: true)
                    }
                    .sortedByDescending { transactionDto ->
                        try {
                            Instant.parse(transactionDto.createdAt)
                        } catch (e: DateTimeParseException) {
                            Log.e("SpendScanApp", "SORTING ERROR: Could not parse createdAt '${transactionDto.createdAt}'. Using Instant.MIN.", e)
                            Instant.MIN
                        } catch (e: Exception) {
                            Log.e("SpendScanApp", "SORTING ERROR: Unexpected error parsing createdAt '${transactionDto.createdAt}'. Using Instant.MIN.", e)
                            Instant.MIN
                        }
                    }

                _transactions.value = onlyExpenses

                var currentTotalAmount = BigDecimal.ZERO
                var currency: String? = null

                _transactions.value.forEach { transactionDto ->
                    try {
                        currentTotalAmount = currentTotalAmount.add(BigDecimal(transactionDto.amount).abs())
                        if (currency == null) {
                            currency = transactionDto.account.currency
                        } else if (currency != transactionDto.account.currency) {
                            Log.w("SpendScanApp", "Transactions have different currencies. This case needs to be handled.")
                        }
                    } catch (e: NumberFormatException) {
                        Log.e("SpendScanApp", "Error parsing amount '${transactionDto.amount}'. Transaction skipped in total calculation.", e)
                    }
                }

                _totalExpenses.value = "$currentTotalAmount ${currency?:""}"
                Log.d("SpendScanApp", "MyHistoryViewModel: Transactions loaded successfully. Count: ${_transactions.value.size}")
            } catch (e: IOException) { // Отлавливаем IOException от репозитория после перезапросов
                _error.value = "Проблема с интернет-соединением. Попробуйте ещё раз."
                Log.e("SpendScanApp", "MyHistoryViewModel: Network error after retries: ${e.message}", e)
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки транзакций: ${e.message ?: "Неизвестная ошибка"}"
                Log.e("SpendScanApp", "MyHistoryViewModel: Error loading transactions: ${e.message}", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}