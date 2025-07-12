package com.spendscan.spendscan.feature.income.ui.viewmodel

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
import com.spendscan.spendscan.feature.income.domain.models.IncomeData
import com.spendscan.spendscan.feature.income.domain.usecase.GetIncomeUseCase
import com.spendscan.spendscan.feature.income.ui.viewmodel.contract.IncomeAction
import com.spendscan.spendscan.feature.income.ui.viewmodel.contract.IncomeState
import java.net.UnknownHostException
import javax.inject.Inject


/**
 * ViewModel для экрана доходов, управляющая ui состоянием
 * @param getIncomeUseCase UseCase получения данных о доходах
 */
class IncomeViewModel @Inject constructor(
    private val getIncomeUseCase: GetIncomeUseCase,
    private val getAccountInfoUseCase: GetAccountInfoUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<IncomeState>(IncomeState())
    val state = _state.asStateFlow()

    init {
        getIncome()
    }

    fun onAction(action: IncomeAction) {
        when (action) {
            IncomeAction.Refresh -> getIncome()
        }
    }

    private fun getIncome() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _state.update { it.copy(isLoading = true, errorMessage = null) }
                var result: Result<IncomeData> = Result.Success<IncomeData>(IncomeData())
                getAccountInfoUseCase().onSuccess { res ->
                    _state.update { it.copy(currency = res.currency) }
                    result = getIncomeUseCase(res.id)
                }.onError { code, message ->
                    result = Result.Error(code, message)
                }.onException {
                    result = Result.Exception(it)
                }
                handleResult(result)

            }
        }
    }

    private fun handleResult(result: Result<IncomeData>) {
        result.onSuccess { expenseData ->
                _state.update {
                    it.copy(
                        incomes = expenseData.income,
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
