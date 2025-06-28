import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spendscan.core.common.Result
import com.spendscan.core.domain.models.Transaction
import com.spendscan.features.expenses.useCase.GetTodayExpensesUseCase
import com.spendscan.core.network.ConnectivityObserver
import com.spendscan.core.network.NetworkStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class ExpensesViewModel(
    private val getTodayExpensesUseCase: GetTodayExpensesUseCase,
    private val accountId: Int,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _totalExpenses = MutableStateFlow("")
    val totalExpenses: StateFlow<String> = _totalExpenses.asStateFlow()

    private val _isOnline = MutableStateFlow(true)
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    init {
        viewModelScope.launch {
            connectivityObserver.observe().collect { status ->
                _isOnline.value = (status == NetworkStatus.Available || status == NetworkStatus.Losing)
                Log.d("SpendScanApp", "Network Status for TodayExpenses: $status, isOnline: ${_isOnline.value}")
                if (_isOnline.value && _error.value != null && _error.value!!.contains("Сетевая ошибка")) {
                    Log.d("SpendScanApp", "TodayExpenses: Network re-established, attempting to reload expenses.")
                    loadTodayExpenses()
                }
            }
        }
        loadTodayExpenses()
    }

    private fun loadTodayExpenses() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            if (!_isOnline.value) {
                _error.value = "Сетевая ошибка: Отсутствует подключение к интернету."
                _isLoading.value = false
                Log.w("SpendScanApp", "TodayExpenses: Attempted to load expenses while offline.")
                return@launch
            }

            when (val result = getTodayExpensesUseCase(accountId)) {
                is Result.Success -> {
                    _transactions.value = result.data.transactions
                    _totalExpenses.value = "${result.data.totalAmount} ${result.data.currency}"
                    _isLoading.value = false
                }
                is Result.Error -> {
                    _error.value = "Ошибка ${result.code}: ${result.message}"
                    _isLoading.value = false
                    Log.e("SpendScanApp", "TodayExpensesViewModel: Ошибка при загрузке расходов за сегодня: ${result.message}")
                }
                is Result.Exception -> {
                    _error.value = "Исключение: ${result.error.localizedMessage ?: "Неизвестная ошибка"}"
                    _isLoading.value = false
                    Log.e("SpendScanApp", "TodayExpensesViewModel: Исключение при загрузке расходов за сегодня: ${result.error.message}", result.error)
                }
            }
        }
    }
}