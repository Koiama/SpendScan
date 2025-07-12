package com.spendscan.spendscan.feature.expenses.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.spendscan.spendscan.core.common.utils.Result
import com.spendscan.spendscan.core.common.utils.onError
import com.spendscan.spendscan.core.common.utils.onException
import com.spendscan.spendscan.core.common.utils.onSuccess
import com.spendscan.spendscan.core.domain.usecase.GetAccountInfoUseCase
import com.spendscan.spendscan.feature.expenses.domain.models.ExpenseData
import com.spendscan.spendscan.feature.expenses.domain.usecase.GetExpensesUseCase
import com.spendscan.spendscan.feature.expenses.ui.viewmodel.contract.ExpensesAction
import com.spendscan.spendscan.feature.expenses.ui.viewmodel.contract.ExpensesState
import java.net.UnknownHostException
import javax.inject.Inject

/**
 * ViewModel для экрана расходов, управляющая ui состоянием
 * @param getExpensesUseCase UseCase получения данных о расходах
 */
class ExpensesViewModel @Inject constructor(
    private val getExpensesUseCase: GetExpensesUseCase,
    private val getAccountInfoUseCase: GetAccountInfoUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<ExpensesState>(ExpensesState())
    val state = _state.asStateFlow()

    init {
        getExpenses()
    }

    fun onAction(action: ExpensesAction) {
        when (action) {
            ExpensesAction.Refresh -> getExpenses()
        }
    }

    private fun getExpenses() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _state.update { it.copy(isLoading = true, errorMessage = null) }
                var result: Result<ExpenseData> = Result.Success<ExpenseData>(ExpenseData())
                getAccountInfoUseCase().onSuccess { res ->
                    _state.update { it.copy(currency = res.currency) }
                    result = getExpensesUseCase(res.id)
                }.onError { code, message ->
                    result = Result.Error(code, message)
                }.onException {
                    result = Result.Exception(it)
                }
                handleResult(result)
            }
        }
    }

    private fun handleResult(result: Result<ExpenseData>) {
        result.onSuccess { expenseData ->
            _state.update {
                it.copy(
                    expenses = expenseData.expenses,
                    amount = expenseData.amount,
                    isLoading = false
                )
            }
        }.onError { code, message ->
            _state.update {
                it.copy(
                    isLoading = false,
                    errorMessage = "Ошибка: $message"
                )
            }
        }.onException { error ->
            var message = "Ошибка: Непредвиденная ошибка :("
            if (error is UnknownHostException) {
                message = "Ошибка подключения к сети"
            }
            _state.update {
                it.copy(
                    isLoading = false,
                    errorMessage = message
                )
            }
        }
    }
}
