package com.spendscan.features.incomes.presentation


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spendscan.core.common.Result
import com.spendscan.core.domain.models.Transaction
import com.spendscan.core.network.ConnectivityObserver
import com.spendscan.core.network.NetworkStatus
import com.spendscan.features.incomes.useCase.GetTodayIncomeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// TodayIncomeViewModel: ViewModel для отображения доходов за текущий день.
class TodayIncomeViewModel(
    // ИЗМЕНЕНИЕ: Теперь ViewModel принимает UseCase в конструкторе
    private val getTodayIncomeUseCase: GetTodayIncomeUseCase,
    // ИЗМЕНЕНИЕ: accountId теперь Int, так как ваш репозиторий и UseCase ожидают Int
    private val accountId: Int,
    // ИЗМЕНЕНИЕ: Добавляем ConnectivityObserver для проверки сети
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList()) // ИЗМЕНЕНИЕ: List<Transaction> вместо List<TransactionDto>
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _totalIncome = MutableStateFlow("")
    val totalIncome: StateFlow<String> = _totalIncome.asStateFlow()

    private val _isOnline = MutableStateFlow(true) // Состояние онлайн/офлайн
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    init {
        // Запускаем наблюдение за состоянием сети
        viewModelScope.launch {
            connectivityObserver.observe().collect { status ->
                _isOnline.value = (status == NetworkStatus.Available || status == NetworkStatus.Losing)
                Log.d("SpendScanApp", "Network Status for TodayIncome: $status, isOnline: ${_isOnline.value}")
                // Если подключение восстановилось и была сетевая ошибка, пробуем перезагрузить
                if (_isOnline.value && _error.value != null && _error.value!!.contains("Сетевая ошибка")) {
                    Log.d("SpendScanApp", "TodayIncome: Network re-established, attempting to reload income.")
                    loadTodayIncome()
                }
            }
        }
        loadTodayIncome()
    }

    // ИЗМЕНЕНИЕ: Метод теперь вызывает UseCase
    private fun loadTodayIncome() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            // Проверяем статус сети перед вызовом UseCase
            if (!_isOnline.value) {
                _error.value = "Сетевая ошибка: Отсутствует подключение к интернету."
                _isLoading.value = false
                Log.w("SpendScanApp", "TodayIncome: Attempted to load income while offline.")
                return@launch
            }

            // Вызываем UseCase для получения данных
            when (val result = getTodayIncomeUseCase(accountId)) {
                is Result.Success -> {
                    // Данные получены от UseCase
                    _transactions.value = result.data.transactions
                    _totalIncome.value = "${result.data.totalAmount} ${result.data.currency}"
                    _isLoading.value = false
                }
                is Result.Error -> {
                    // Обработка ошибки, полученной от UseCase
                    _error.value = "Ошибка ${result.code}: ${result.message}"
                    _isLoading.value = false
                    Log.e("SpendScanApp", "TodayIncomeViewModel: Ошибка при загрузке доходов за сегодня: ${result.message}")
                }
                is Result.Exception -> {
                    // Обработка исключения, полученного от UseCase
                    _error.value = "Исключение: ${result.error.localizedMessage ?: "Неизвестная ошибка"}"
                    _isLoading.value = false
                    Log.e("SpendScanApp", "TodayIncomeViewModel: Исключение при загрузке доходов за сегодня: ${result.error.message}", result.error)
                }
            }
        }
    }
}