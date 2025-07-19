package com.spendscan.spendscan.feature.edit_balance.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.spendscan.spendscan.core.common.utils.Result
import com.spendscan.spendscan.core.common.utils.format.formatMoney
import com.spendscan.spendscan.core.common.utils.onError
import com.spendscan.spendscan.core.common.utils.onSuccess
import com.spendscan.spendscan.core.common.utils.format.toValidBalance
import com.spendscan.spendscan.core.domain.models.account.AccountBrief
import com.spendscan.spendscan.core.domain.models.account.AccountUpdateInfo
import com.spendscan.spendscan.core.domain.usecase.GetAccountInfoUseCase
import com.spendscan.spendscan.feature.edit_balance.domain.usecase.UpdateAccountByIdUseCase
import com.spendscan.spendscan.feature.edit_balance.ui.viewmodel.contract.EditScreenAction
import com.spendscan.spendscan.feature.edit_balance.ui.viewmodel.contract.EditScreenUiEffect
import com.spendscan.spendscan.feature.edit_balance.ui.viewmodel.contract.EditScreenUiState
import javax.inject.Inject

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
                it.copy(
                    isBottomSheetShown = !it.isBottomSheetShown
                )
            }
        }
    }

    private fun loadAccountInfo() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _uiState.update { it.copy(isLoading = true) }
                val accountInfoResult = getAccountInfoUseCase()
                when (accountInfoResult) {
                    is Result.Error -> _uiEffect.emit(EditScreenUiEffect.ShowError(accountInfoResult.message))
                    is Result.Exception -> Log.e("FATAL ERROR", accountInfoResult.error.toString())
                    is Result.Success<AccountBrief> -> {
                        val accountData = accountInfoResult.data
                        Log.d("SUCCESS", "loadAccountInfo: $accountData")
                        _uiState.update {
                            it.copy(
                                accountId = accountData.id,
                                amountInput = formatMoney(accountData.balance, withSpaces = false),
                                currency = accountData.currency,
                                accountName = accountData.name
                            )
                        }
                    }
                }
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun updateAccountInfo(onSuccess: () -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val state = _uiState.value
                updateAccountByIdUseCase(
                    state.accountId,
                    accountInfo = AccountUpdateInfo(
                        name = state.accountName,
                        balance = state.amountInput,
                        currency = state.currency.name
                    )
                ).onSuccess {
                    Log.d("DEBUG", "$it")
                    withContext(Dispatchers.Main.immediate) {
                        onSuccess()
                    }
                }.onError { c, m ->
                    _uiEffect.emit(EditScreenUiEffect.ShowError("Не удалось обновить счет"))
                }
            }
        }
    }

    private fun updateBalance(newBalance: String) {
        _uiState.update { it.copy(amountInput = newBalance.toValidBalance()) }
    }


}