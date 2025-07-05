package com.spendscan.features.myHistory.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spendscan.core.common.Result
import com.spendscan.core.domain.managers.GlobalCurrentAccountManager
import com.spendscan.core.domain.models.Transaction
import com.spendscan.core.network.ConnectivityObserver
import com.spendscan.core.network.NetworkStatus
import com.spendscan.features.myHistory.useCase.GetTransactionsByTypeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * ViewModel для отображения истории транзакций (расходов или доходов) за определенный период.
 * Отвечает за управление состоянием UI и делегирование бизнес-логики UseCase.
 */
class MyHistoryViewModel(
    private val getTransactionsByTypeUseCase: GetTransactionsByTypeUseCase,
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

    private val _currentAccountId = MutableStateFlow<Int?>(null)

    init {
        // Запускаем наблюдение за состоянием сети
        viewModelScope.launch {
            connectivityObserver.observe().collect { status ->
                _isOnline.value = (status == NetworkStatus.Available || status == NetworkStatus.Losing)
                // Если сеть восстановилась И была ошибка, попробуем перезагрузить
                if (_isOnline.value && _error.value != null && _error.value!!.contains("Сетевая ошибка")) {
                    Log.d("SpendScanApp", "Network re-established, attempting to reload transactions.")
                    loadTransactions()
                }
            }
        }

        // Запускаем наблюдение за ID текущего аккаунта из GlobalCurrentAccountManager
        // Используем collectLatest, чтобы отменить предыдущие запросы, если ID быстро меняется
        viewModelScope.launch {
            GlobalCurrentAccountManager.instance.currentAccountId.collectLatest { id ->
                _currentAccountId.value = id
                if (id != null) {
                    Log.d("MyHistoryViewModel", "Account ID received: $id. Loading transactions.")
                    loadTransactions() // Загружаем транзакции, как только ID аккаунта станет доступен
                } else {
                    Log.d(
                        "MyHistoryViewModel",
                        "Account ID is null. Waiting for account to be loaded."
                    )
                    _transactions.value = emptyList() // Очищаем список, если ID стал null
                    _totalAmount.value = 0.0
                    _totalFormatted.value = ""
                    _isLoading.value = false
                    _error.value = "Аккаунт не выбран или не загружен."
                }
            }
        }
    }

    // <-- ЭТА ФУНКЦИЯ ТЕПЕРЬ НАХОДИТСЯ НА УРОВНЕ КЛАССА MyHistoryViewModel
    fun setDateRange(newStartDate: LocalDate, newEndDate: LocalDate) {
        _startDate.value = newStartDate
        _endDate.value = newEndDate
        loadTransactions() // Перезагружаем транзакции с новой датой
    }

    /**
     * Загружает и отображает транзакции за текущий период и тип.
     * <-- ЭТА ФУНКЦИЯ ТЕПЕРЬ ТАКЖЕ НАХОДИТСЯ НА УРОВНЕ КЛАССА MyHistoryViewModel
     * (В прошлой версии у вас была одна 'loadTransactions()'
     * и внутри неё вложенная 'loadTransactions()'. Мы их объединили.)
     */
    fun loadTransactions() {
        val accountId = _currentAccountId.value // Получаем текущий accountId из StateFlow
        if (accountId == null) {
            Log.w(
                "MyHistoryViewModel",
                "loadTransactions() called but current account ID is null. Skipping load."
            )
            _isLoading.value = false
            _error.value = "Ошибка: ID аккаунта не загружен."
            return // Не загружаем, если ID аккаунта еще не установлен
        }

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
                    _totalFormatted.value =
                        "${transactionData.amount} ${transactionData.currency}"
                    _isLoading.value = false
                    Log.d(
                        "SpendScanApp",
                        "TransactionHistoryViewModel: Transactions successfully loaded and calculated."
                    )
                }

                is Result.Error -> {
                    _error.value = "Ошибка ${result.code}: ${result.message}"
                    _isLoading.value = false
                    Log.e(
                        "SpendScanApp",
                        "TransactionHistoryViewModel: Error loading transactions: ${result.message}"
                    )
                }

                is Result.Exception -> {
                    _error.value =
                        "Исключение: ${result.error.localizedMessage ?: "Неизвестная ошибка"}"
                    _isLoading.value = false
                    Log.e(
                        "SpendScanApp",
                        "TransactionHistoryViewModel: Exception loading transactions: ${result.error.message}",
                        result.error
                    )
                }
            }
        }
    }
}