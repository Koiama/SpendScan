package com.spendscan.features.incomes.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spendscan.core.common.Result
import com.spendscan.core.domain.managers.GlobalCurrentAccountManager
import com.spendscan.core.domain.models.Transaction
import com.spendscan.core.network.ConnectivityObserver
import com.spendscan.core.network.NetworkStatus
import com.spendscan.features.incomes.useCase.GetTodayIncomeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TodayIncomeViewModel(
    private val getTodayIncomeUseCase: GetTodayIncomeUseCase,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _totalIncome = MutableStateFlow("")
    val totalIncome: StateFlow<String> = _totalIncome.asStateFlow()

    private val _isOnline = MutableStateFlow(true)
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    private val _currentAccountId = MutableStateFlow<Int?>(null)

    init {
        viewModelScope.launch {
            connectivityObserver.observe().collect { status ->
                _isOnline.value = (status == NetworkStatus.Available || status == NetworkStatus.Losing)
                if (_isOnline.value && _error.value != null && _error.value!!.contains("Сетевая ошибка")) {
                    loadTodayIncome()
                }
            }
        }

        viewModelScope.launch {
            GlobalCurrentAccountManager.instance.currentAccountId.collectLatest { id ->
                _currentAccountId.value = id
                if (id != null) {
                    Log.d("TodayIncomeViewModel", "Account ID received: $id. Loading today's incomes.")
                    loadTodayIncome()
                } else {
                    Log.d("TodayIncomeViewModel", "Account ID is null. Waiting for account to be loaded.")
                    _transactions.value = emptyList()
                    _totalIncome.value = ""
                    _isLoading.value = false
                    _error.value = "Аккаунт не выбран или не загружен."
                }
            }
        }
    }

    fun loadTodayIncome() {
        val accountId = _currentAccountId.value
        if (accountId == null) {
            Log.w("TodayIncomeViewModel", "loadTodayIncome() called but current account ID is null. Skipping load.")
            _isLoading.value = false
            _error.value = "Ошибка: ID аккаунта не загружен."
            return
        }

        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            if (!_isOnline.value) {
                _error.value = "Сетевая ошибка: Отсутствует подключение к интернету."
                _isLoading.value = false
                return@launch
            }

            when (val result = getTodayIncomeUseCase(accountId)) {
                is Result.Success -> {
                    _transactions.value = result.data.transactions
                    _totalIncome.value = "${result.data.totalAmount} ${result.data.currency}"
                    _isLoading.value = false
                    Log.d("TodayIncomeViewModel", "Incomes loaded successfully for account ID: $accountId")
                }
                is Result.Error -> {
                    _error.value = "Ошибка ${result.code}: ${result.message}"
                    _isLoading.value = false
                    Log.e("TodayIncomeViewModel", "Error loading incomes: ${result.message}")
                }
                is Result.Exception -> {
                    _error.value = "Исключение: ${result.error.localizedMessage ?: "Неизвестная ошибка"}"
                    _isLoading.value = false
                    Log.e("TodayIncomeViewModel", "Exception loading incomes: ${result.error.message}", result.error)
                }
            }
        }
    }
}