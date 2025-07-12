package com.spendscan.spendscan.feature.history.ui.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.spendscan.spendscan.core.common.utils.Result
import com.spendscan.spendscan.core.common.utils.onError
import com.spendscan.spendscan.core.common.utils.onException
import com.spendscan.spendscan.core.common.utils.onSuccess
import com.spendscan.spendscan.core.domain.models.transaction.TransactionType
import com.spendscan.spendscan.core.domain.usecase.GetAccountInfoUseCase
import com.spendscan.spendscan.feature.history.domain.models.HistoryData
import com.spendscan.spendscan.feature.history.domain.usecase.GetHistoryUseCase
import com.spendscan.spendscan.feature.history.ui.viewmodel.contract.HistoryAction
import com.spendscan.spendscan.feature.history.ui.viewmodel.contract.HistoryState
import java.net.UnknownHostException
import java.time.LocalDate
import javax.inject.Inject

/**
 * ViewModel для экрана истории, управляющая ui состоянием
 * @param getHistoryUseCase UseCase получения данных о истории доходов/расходов за опред. период
 */
class HistoryViewModel @Inject constructor(
    private val getHistoryUseCase: GetHistoryUseCase,
    private val getAccountInfoUseCase: GetAccountInfoUseCase
) : ViewModel() {

    var transactionType: TransactionType = TransactionType.EXPENSE

    private val _state: MutableStateFlow<HistoryState> = MutableStateFlow(HistoryState())
    val state: StateFlow<HistoryState> = _state.asStateFlow()

    init {
        loadTransactions()
    }

    fun onAction(action: HistoryAction) {
        when (action) {
            HistoryAction.LoadTransactions -> loadTransactions()
            HistoryAction.ChangeDatePickerVisibility -> _state.update {
                it.copy(isDatePickerShown = !it.isDatePickerShown)
            }
            is HistoryAction.UpdateDateEnd -> updateDateEnd(action.date)
            is HistoryAction.UpdateDateStart -> updateDateStart(action.date)
        }
    }

    private fun loadTransactions() {
        val state = _state.value

        _state.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                var result: Result<HistoryData> = Result.Success(HistoryData())
                getAccountInfoUseCase().onSuccess { res ->
                    _state.update { it.copy(currency = res.currency) }
                    result = getHistoryUseCase(
                        startDate = state.dateStart,
                        endDate = state.dateEnd,
                        accountId = res.id,
                        transactionType = transactionType
                    )
                }.onError { code, message ->
                    result = Result.Error(code, message)
                }.onException {
                    result = Result.Exception(it)
                }
                handleResult(result)
            }
        }
    }

    private fun handleResult(result: Result<HistoryData>) {
        result.onSuccess { historyData ->
            _state.update {
                it.copy(
                    transactions = historyData.transactions,
                    amount = historyData.amount,
                    isLoading = false
                )
            }
        }.onError { code, message ->
            _state.update {
                it.copy(
                    isLoading = false,
                    errorMessage = "Ошибка $code: $message"
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

    private fun updateDateEnd(dateEnd: LocalDate) {
        _state.update { state ->
            val newEnd = if (dateEnd.isBefore(state.dateStart)) state.dateStart else dateEnd
            state.copy(dateEnd = newEnd)
        }
        loadTransactions()
    }

    private fun updateDateStart(dateStart: LocalDate) {
        _state.update { state ->
            val newStart = if (dateStart.isAfter(state.dateEnd)) state.dateEnd else dateStart
            state.copy(dateStart = newStart)
        }
        loadTransactions()
    }
}
