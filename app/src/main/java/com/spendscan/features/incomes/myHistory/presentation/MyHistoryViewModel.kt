package com.spendscan.features.incomes.myHistory.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spendscan.features.incomes.myHistory.data.models.TransactionDto
import com.spendscan.features.incomes.myHistory.domain.MyHistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MyHistoryViewModel(
    private val repository: MyHistoryRepository
) : ViewModel() {

    // MutableStateFlow для хранения списка транзакций, которые будут отображаться в UI
    private val _transactions = MutableStateFlow<List<TransactionDto>>(emptyList())
    val transactions: StateFlow<List<TransactionDto>> = _transactions.asStateFlow()

    // MutableStateFlow для отслеживания состояния загрузки
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // MutableStateFlow для отслеживания ошибок
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()


    fun loadTransactions(accountId: String, startDate: String? = null, endDate: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null // Сбрасываем предыдущие ошибки

            try {
                val fetchedTransactions =
                    repository.getTransactionsByPeriod(accountId, startDate, endDate)
                _transactions.value = fetchedTransactions.sortedByDescending { it.transactionDate }
                //transactionDate сейчас String. Для корректной сортировки нужно будет либо парсить его в LocalDate или LocalDateTime при сортировке, либо маппить TransactionDto в доменную модель, где даты уже будут нужного типа. Пока String с ISO-форматом будет сортироваться лексикографически правильно, но это не всегда надежно.
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

