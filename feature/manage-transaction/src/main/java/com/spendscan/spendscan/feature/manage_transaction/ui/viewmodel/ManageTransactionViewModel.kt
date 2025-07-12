package com.spendscan.spendscan.feature.manage_transaction.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.spendscan.spendscan.core.common.utils.Result
import com.spendscan.spendscan.core.common.utils.onSuccess
import com.spendscan.spendscan.core.common.utils.validator.toValidBalance
import com.spendscan.spendscan.core.domain.usecase.GetAccountInfoUseCase
import com.spendscan.spendscan.feature.manage_transaction.domain.usecase.CreateTransactionUseCase
import com.spendscan.spendscan.feature.manage_transaction.domain.usecase.DeleteTransactionUseCase
import com.spendscan.spendscan.feature.manage_transaction.domain.usecase.GetCategoriesByTypeUseCase
import com.spendscan.spendscan.feature.manage_transaction.domain.usecase.GetTransactionByIdUseCase
import com.spendscan.spendscan.feature.manage_transaction.domain.usecase.UpdateTransactionUseCase
import com.spendscan.spendscan.feature.manage_transaction.ui.viewmodel.contract.ManageTransactionAction
import com.spendscan.spendscan.feature.manage_transaction.ui.viewmodel.contract.ManageTransactionUiState
import java.time.LocalDateTime

@Suppress("LongParameterList")
class ManageTransactionViewModel @AssistedInject constructor(
    @Assisted private val isIncome: Boolean,
    @Assisted private val transactionId: Long?,
    private val getCategoriesByTypeUseCase: GetCategoriesByTypeUseCase,
    private val getTransactionById: GetTransactionByIdUseCase,
    private val getAccountInfoUseCase: GetAccountInfoUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val createTransactionUseCase: CreateTransactionUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ManageTransactionUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            withContext(Dispatchers.IO) {
                getCategoriesByTypeUseCase(isIncome).onSuccess { categories ->
                    _uiState.update { it.copy(categories = categories) }
                    if (categories.isNotEmpty() && transactionId == null) {
                        _uiState.update { it.copy(category = categories[0]) }
                    }
                }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
        transactionId?.let {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true) }
                withContext(Dispatchers.IO) {
                    getTransactionById(transactionId).onSuccess { transaction ->
                        transaction?.let {
                            _uiState.update {
                                it.copy(
                                    category = transaction.category,
                                    dateTime = transaction.date,
                                    stringAmount = transaction.amount.toString(),
                                    description = transaction.comment ?: ""
                                )
                            }
                        }
                    }
                }
                _uiState.update { it.copy(isLoading = false) }
            }
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            withContext(Dispatchers.IO) {
                getAccountInfoUseCase().onSuccess { info ->
                    _uiState.update {
                        it.copy(
                            accountName = info.name,
                            currency = info.currency,
                            accountId = info.id
                        )
                    }
                }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    @AssistedFactory
    interface ViewModelAssistedFactory {
        fun create(isIncomeScreen: Boolean, transactionId: Long?): ManageTransactionViewModel
    }

    fun onAction(action: ManageTransactionAction) {
        when (action) {
            ManageTransactionAction.ChangeDatePickerVisibility -> _uiState.update {
                it.copy(
                    isDatePickerShown = !it.isDatePickerShown
                )
            }

            ManageTransactionAction.ChangeTimePickerVisibility -> _uiState.update {
                it.copy(isTimePickerShown = !it.isTimePickerShown)
            }

            ManageTransactionAction.ChangeDropdownState -> _uiState.update {
                it.copy(isDropdownMenuExpanded = !it.isDropdownMenuExpanded)
            }

            is ManageTransactionAction.UpdateDate -> _uiState.update {
                it.copy(
                    dateTime = LocalDateTime.of(action.newDate, it.dateTime.toLocalTime())
                )
            }

            is ManageTransactionAction.UpdateDescription -> _uiState.update {
                if (action.newDescription.length < DESCRIPTION_THRESHOLD) {
                    it.copy(description = action.newDescription)
                } else {
                    it
                }
            }

            is ManageTransactionAction.UpdateTime -> _uiState.update {
                it.copy(dateTime = LocalDateTime.of(it.dateTime.toLocalDate(), action.newTime))
            }

            is ManageTransactionAction.UpdateTotalAmount -> _uiState.update {
                it.copy(stringAmount = action.newAmount.toValidBalance())
            }


            is ManageTransactionAction.UpdateCategory -> _uiState.update {
                it.copy(category = action.newCategory)
            }

            is ManageTransactionAction.CompleteEdit -> handleSaving(action.onSuccess)
            is ManageTransactionAction.DeleteTransaction -> deleteTransaction(action.onSuccess)
        }
    }

    private fun handleSaving(onSuccess: () -> Unit) {
        viewModelScope.launch {
            var res: Result<Unit> = Result.Success(Unit)
            withContext(Dispatchers.IO) {
                val state = _uiState.value
                if (transactionId == null) {
                    res = createTransactionUseCase(
                        accountId = state.accountId,
                        categoryId = state.category.id,
                        amount = state.stringAmount,
                        transactionDate = state.dateTime,
                        comment = state.description
                    )
                } else {
                    res = updateTransactionUseCase(
                        transactionId = transactionId,
                        accountId = state.accountId,
                        categoryId = state.category.id,
                        amount = state.stringAmount,
                        transactionDate = state.dateTime,
                        comment = state.description
                    )
                }
            }
            res.onSuccess {
                onSuccess()
            }
        }
    }

    private fun deleteTransaction(onSuccess: () -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                transactionId?.let {
                    deleteTransactionUseCase(transactionId).onSuccess {
                        delay(50L)
                        withContext(Dispatchers.Main.immediate) {
                            onSuccess()
                        }
                    }
                }
            }
        }
    }

    private companion object {
        private const val DESCRIPTION_THRESHOLD = 25
    }
}