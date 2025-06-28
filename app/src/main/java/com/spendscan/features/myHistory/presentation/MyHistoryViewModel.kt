package com.spendscan.features.myHistory.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spendscan.core.common.Result
import com.spendscan.core.domain.models.Transaction
import com.spendscan.core.network.ConnectivityObserver
import com.spendscan.core.network.NetworkStatus
import com.spendscan.features.myHistory.useCase.GetTransactionsByTypeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * ViewModel для отображения истории транзакций (расходов или доходов) за определенный период.
 * Отвечает за управление состоянием UI и делегирование бизнес-логики UseCase.
 */
class MyHistoryViewModel(
    private val getTransactionsByTypeUseCase: GetTransactionsByTypeUseCase,
    private val accountId: Int,
    private val isIncome: Boolean? = null,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _totalAmount = MutableStateFlow(0.0)
    private val _totalFormatted = MutableStateFlow("")
    val totalFormatted: StateFlow<String> = _totalFormatted.asStateFlow()

    private val _startDate = MutableStateFlow(LocalDate.now().minusMonths(1))
    val startDate: StateFlow<LocalDate> = _startDate.asStateFlow()

    private val _endDate = MutableStateFlow(LocalDate.now())
    val endDate: StateFlow<LocalDate> = _endDate.asStateFlow()

    // Состояние онлайн/офлайн
    private val _isOnline = MutableStateFlow(true)
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    init {
        // Запускаем наблюдение за состоянием сети
        viewModelScope.launch {
            connectivityObserver.observe().collect { status ->
                _isOnline.value = (status == NetworkStatus.Available || status == NetworkStatus.Losing)
                Log.d("SpendScanApp", "Network Status: $status, isOnline: ${_isOnline.value}")
                if (_isOnline.value && _error.value != null && _error.value!!.contains("Сетевая ошибка")) {
                    Log.d("SpendScanApp", "Network re-established, attempting to reload transactions.")
                    loadTransactions()
                }
            }
        }
        loadTransactions()
    }

    fun setDateRange(newStartDate: LocalDate, newEndDate: LocalDate) {
        _startDate.value = newStartDate
        _endDate.value = newEndDate
        loadTransactions()
    }

    /**
     * Загружает и отображает транзакции за текущий период и тип.
     */
    fun loadTransactions() {
        _isLoading.value = true
        _error.value = null // Сбрасываем ошибку перед новой попыткой

        viewModelScope.launch {
            // Проверяем статус сети перед запросом
            if (!_isOnline.value) {
                _error.value = "Сетевая ошибка: Отсутствует подключение к интернету."
                _isLoading.value = false
                Log.w("SpendScanApp", "Attempted to load transactions while offline.")
                return@launch // Выходим, если нет сети
            }

            when (val result = getTransactionsByTypeUseCase(
                accountId = accountId,
                startDate = _startDate.value,
                endDate = _endDate.value,
                isIncome = isIncome
            )) {
                is Result.Success -> {
                    val transactionData = result.data
                    _transactions.value = transactionData.expenses
                    _totalAmount.value = transactionData.amount
                    _totalFormatted.value = "${transactionData.amount} ${transactionData.currency}"
                    _isLoading.value = false
                    Log.d("SpendScanApp", "TransactionHistoryViewModel: Transactions successfully loaded and calculated.")
                }
                is Result.Error -> {
                    _error.value = "Ошибка ${result.code}: ${result.message}"
                    _isLoading.value = false
                    Log.e("SpendScanApp", "TransactionHistoryViewModel: Error loading transactions: ${result.message}")
                }
                is Result.Exception -> {
                    _error.value = "Исключение: ${result.error.localizedMessage ?: "Неизвестная ошибка"}"
                    _isLoading.value = false
                    Log.e("SpendScanApp", "TransactionHistoryViewModel: Exception loading transactions: ${result.error.message}", result.error)
                }
            }
        }
    }
}