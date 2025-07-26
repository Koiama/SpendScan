package com.spendscan.spendscan.feature.balance.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spendscan.spendscan.core.common.utils.Result
import com.spendscan.spendscan.core.common.utils.format.formatMoney
import com.spendscan.spendscan.core.common.utils.onError
import com.spendscan.spendscan.core.common.utils.onException
import com.spendscan.spendscan.core.common.utils.onSuccess
import com.spendscan.spendscan.core.domain.models.account.AccountBrief
import com.spendscan.spendscan.core.domain.models.transaction.Transaction // Импорт для Transaction
import com.spendscan.spendscan.core.domain.usecase.GetAccountInfoUseCase
import com.spendscan.spendscan.feature.balance.domain.GetTransactionsForLastMonthUseCase
import com.spendscan.spendscan.feature.balance.ui.viewmodel.contract.BalanceEffect // Создадим BalanceEffect
import com.spendscan.spendscan.feature.balance.ui.viewmodel.contract.BalanceState
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow // Для эффектов
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow // Для эффектов
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel для экрана баланса, управляющая состоянием данных
 */
class BalanceViewModel @Inject constructor(
    private val getAccountInfoUseCase: GetAccountInfoUseCase,
    private val getTransactionsForLastMonthUseCase: GetTransactionsForLastMonthUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(BalanceState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<BalanceEffect>()
    val effect = _effect.asSharedFlow()

    init {

        loadComprehensiveBalanceData()
    }
    /**
     * Функция для загрузки информации о счете и транзакциях за последний месяц.
     */
    fun loadComprehensiveBalanceData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            var accountIdForTransactions: Long? = null

            // Шаг 1: Загрузка информации о счете
            when (val accountResult = getAccountInfoUseCase()) {
                is Result.Success<AccountBrief> -> {
                    val accountData = accountResult.data
                    Log.d("BalanceVM_loadComprehensive", "Account info loaded: $accountData")
                    _state.update {
                        it.copy(
                            amount = formatMoney(accountData.balance),
                            accountName = accountData.name,
                            currency = accountData.currency,
                            accountId = accountData.id
                        )
                    }
                    if (accountData.id != 0L) {
                        accountIdForTransactions = accountData.id
                    } else {
                        Log.w("BalanceVM_loadComprehensive", "Account ID is 0, transactions will not be loaded.")
                        _state.update { it.copy(isLoading = false) }
                    }
                }
                is Result.Error -> {
                    Log.e("BalanceVM_loadComprehensive", "Error loading account info: ${accountResult.code} ${accountResult.message}")
                    val errorMessage = accountResult.message ?: "Ошибка загрузки данных счета"
                    _state.update { it.copy(isLoading = false, error = errorMessage) }
                    _effect.emit(BalanceEffect.ShowError(errorMessage))
                    return@launch
                }
                is Result.Exception -> {
                    Log.e("BalanceVM_loadComprehensive", "Exception loading account info: ${accountResult.error.message}", accountResult.error)
                    val errorMessage = "Исключение при загрузке счета: ${accountResult.error.message ?: "Неизвестная ошибка"}"
                    _state.update { it.copy(isLoading = false, error = errorMessage) }
                    _effect.emit(BalanceEffect.ShowError(errorMessage))
                    return@launch
                }
            }

            // Шаг 2: Загрузка транзакций, если ID счета получен
            if (accountIdForTransactions != null) {
                Log.d("BalanceVM_loadComprehensive", "Loading transactions for account ID: $accountIdForTransactions")
                when (val transactionsResult = getTransactionsForLastMonthUseCase(accountId = accountIdForTransactions)) {
                    is Result.Success<List<Transaction>> -> {
                        Log.d("BalanceVM_loadComprehensive", "Transactions loaded: ${transactionsResult.data.size} items")
                        _state.update {
                            it.copy(
                                isLoading = false, // Конец общей загрузки
                                transactions = transactionsResult.data,
                                error = null
                            )
                        }
                    }
                    is Result.Error -> {
                        Log.e("BalanceVM_loadComprehensive", "Error loading transactions: ${transactionsResult.code} ${transactionsResult.message}")
                        val errorMessage = transactionsResult.message ?: "Ошибка загрузки транзакций"
                        _state.update { it.copy(isLoading = false, error = errorMessage) }
                        _effect.emit(BalanceEffect.ShowError(errorMessage))
                    }
                    is Result.Exception -> {
                        Log.e("BalanceVM_loadComprehensive", "Exception loading transactions: ${transactionsResult.error.message}", transactionsResult.error)
                        val errorMessage = "Исключение при загрузке транзакций: ${transactionsResult.error.message ?: "Неизвестная ошибка"}"
                        _state.update { it.copy(isLoading = false, error = errorMessage) }
                        _effect.emit(BalanceEffect.ShowError(errorMessage))
                    }
                }
            } else {
                if (_state.value.isLoading) {
                    _state.update { it.copy(isLoading = false) }
                }
                Log.d("BalanceVM_loadComprehensive", "Skipping transaction load, account ID not valid or not available.")
            }
        }
    }
}

