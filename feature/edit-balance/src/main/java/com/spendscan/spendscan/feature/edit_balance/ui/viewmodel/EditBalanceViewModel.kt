package com.spendscan.spendscan.feature.edit_balance.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spendscan.spendscan.core.common.utils.Result
import com.spendscan.spendscan.core.common.utils.format.formatMoney
import com.spendscan.spendscan.core.common.utils.format.toValidBalance
import com.spendscan.spendscan.core.common.utils.onError
import com.spendscan.spendscan.core.common.utils.onSuccess
import com.spendscan.spendscan.core.domain.models.account.AccountBrief
import com.spendscan.spendscan.core.domain.models.account.AccountUpdateInfo
import com.spendscan.spendscan.core.domain.models.transaction.Transaction
import com.spendscan.spendscan.core.domain.usecase.GetAccountInfoUseCase
import com.spendscan.spendscan.feature.edit_balance.domain.usecase.UpdateAccountByIdUseCase
import com.spendscan.spendscan.feature.edit_balance.ui.viewmodel.contract.EditScreenAction
import com.spendscan.spendscan.feature.edit_balance.ui.viewmodel.contract.EditScreenUiEffect
import com.spendscan.spendscan.feature.edit_balance.ui.viewmodel.contract.EditScreenUiState
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditBalanceViewModel @Inject constructor(
    private val getAccountInfoUseCase: GetAccountInfoUseCase,
    private val updateAccountByIdUseCase: UpdateAccountByIdUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(EditScreenUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<EditScreenUiEffect>()
    val uiEffect = _uiEffect.asSharedFlow()

    init {
        loadAccountInfo()
    }

    fun loadTransactionsForLastMonth() {
        viewModelScope.launch {
            if (!_uiState.value.isLoading) {
                _uiState.update { it.copy(isLoading = true) }
            }

            val currentAccountId = _uiState.value.accountId
            if (currentAccountId == 0L || currentAccountId == 1L && _uiState.value.accountName == "Мой счет") {
                _uiEffect.emit(EditScreenUiEffect.ShowError("ID счета не загружен или недействителен для получения транзакций"))
                return@launch
            }


        }
    }

    fun onAction(action: EditScreenAction) {
        when (action) {
            is EditScreenAction.UpdateAccountInfo -> updateAccountInfo(action.onSuccess)
            is EditScreenAction.ChangeAccountName -> _uiState.update {
                it.copy(accountName = action.newName)
            }
            is EditScreenAction.ChangeAccountAmount -> updateBalance(action.newAmount)
            is EditScreenAction.ChangeAccountCurrency -> _uiState.update {
                it.copy(currency = action.newCurrency)
            }
            EditScreenAction.ChangeBottomSheetVisibility -> _uiState.update {
                it.copy(isBottomSheetShown = !it.isBottomSheetShown)
            }
            EditScreenAction.LoadTransactionsForLastMonth -> {
                viewModelScope.launch {
                    _uiState.update { it.copy(isLoading = true) }
                    loadTransactionsForLastMonth()
                }
            }
        }
    }

    private fun loadAccountInfo() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            var accountSuccessfullyLoaded = false

            withContext(Dispatchers.IO) {
                when (val accountInfoResult = getAccountInfoUseCase()) {
                    is Result.Error -> {
                        withContext(Dispatchers.Main.immediate) {
                            _uiEffect.emit(EditScreenUiEffect.ShowError(accountInfoResult.message))
                        }
                    }
                    is Result.Exception -> {
                        Log.e(
                            "EDIT_BALANCE_VM",
                            "Loading Account Info Exception: ${accountInfoResult.error.message}",
                            accountInfoResult.error
                        )
                        withContext(Dispatchers.Main.immediate) {
                            _uiEffect.emit(EditScreenUiEffect.ShowError("Исключение при загрузке данных счета: ${accountInfoResult.error.message ?: "Неизвестная ошибка"}"))
                        }
                    }
                    is Result.Success<AccountBrief> -> {
                        val accountData = accountInfoResult.data
                        Log.d("EDIT_BALANCE_VM", "loadAccountInfo success: $accountData")
                        _uiState.update {
                            it.copy(
                                accountId = accountData.id,
                                amountInput = formatMoney(accountData.balance, withSpaces = false),
                                currency = accountData.currency,
                                accountName = accountData.name
                            )
                        }
                        accountSuccessfullyLoaded = true
                        if (accountData.id != 0L) {
                            loadTransactionsForLastMonthInternal()
                        }
                    }
                }
            }
            _uiState.update { it.copy(isLoading = false) } // <--- КОНЕЦ общей загрузки
        }
    }

    // Внутренняя версия для вызова из loadAccountInfo, которая НЕ управляет isLoading
    private suspend fun loadTransactionsForLastMonthInternal() {
        // Ошибки здесь будут обработаны через _uiEffect, isLoading управляется loadAccountInfo
        val currentAccountId = _uiState.value.accountId
        if (currentAccountId == 0L || (currentAccountId == 1L && _uiState.value.accountName == "Мой счет")) {
            _uiEffect.emit(EditScreenUiEffect.ShowError("ID счета недействителен для получения транзакций (внутренний вызов)"))
            return
        }
    }


    private fun updateAccountInfo(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            var success = false
            withContext(Dispatchers.IO) {
                val state = _uiState.value
                if (state.accountId == 0L || (state.accountId == 1L && state.accountName == "Мой счет")) {
                    withContext(Dispatchers.Main.immediate) {
                        _uiEffect.emit(EditScreenUiEffect.ShowError("Невозможно обновить счет: ID счета недействителен."))
                    }
                    // isLoading будет сброшен в finally блоке этой корутины
                    return@withContext
                }

                updateAccountByIdUseCase(
                    accountId = state.accountId,
                    accountInfo = AccountUpdateInfo(
                        name = state.accountName,
                        balance = state.amountInput,
                        currency = state.currency.name
                    )
                ).onSuccess {
                    Log.d("EDIT_BALANCE_VM", "Account updated successfully: $it")
                    success = true
                    withContext(Dispatchers.Main.immediate) {
                        onSuccess()
                        _uiEffect.emit(EditScreenUiEffect.ShowSuccessMessage("Счет успешно обновлен"))
                    }
                }.onError { code, message ->
                    Log.e("EDIT_BALANCE_VM", "Failed to update account. Code: $code, Message: $message")
                    val errorMessage = message ?: "Не удалось обновить счет"
                    withContext(Dispatchers.Main.immediate) {
                        _uiEffect.emit(EditScreenUiEffect.ShowError(errorMessage))
                    }
                }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun updateBalance(newBalance: String) {
        _uiState.update { it.copy(amountInput = newBalance.toValidBalance()) }
    }
}