package com.spendscan.features.account.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spendscan.core.common.onError
import com.spendscan.core.common.onException
import com.spendscan.core.common.onSuccess
import com.spendscan.core.data.repository.AccountRepositoryImpl
import com.spendscan.core.domain.managers.CurrentCurrencyManager
import com.spendscan.core.network.RetrofitClient
import com.spendscan.features.account.domain.GetAccountUseCase
import com.spendscan.features.account.domain.GetAvailableCurrenciesUseCase
import com.spendscan.features.account.domain.UpdateAccountUseCase
import com.spendscan.features.account.domain.models.Currency
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AccountViewModel(
    private val getAccountUseCase: GetAccountUseCase = // Используем ваш обновленный UseCase
        GetAccountUseCase(
            repository = AccountRepositoryImpl(
                accountApiService = RetrofitClient.accountApiService
            )
        ),
    private val updateAccountUseCase: UpdateAccountUseCase =
        UpdateAccountUseCase(
            repository = AccountRepositoryImpl(
                accountApiService = RetrofitClient.accountApiService
            )
        ),
    private val getAvailableCurrenciesUseCase: GetAvailableCurrenciesUseCase =
        GetAvailableCurrenciesUseCase()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState: StateFlow<AccountUiState> = _uiState.asStateFlow()


    init {
        Log.d("AccountViewModel", "ViewModel initialized. Calling loadAccount() and loadAvailableCurrencies().")
        // При инициализации вызываем loadAccount() без аргументов, чтобы получить первый аккаунт
        loadAccount()
        loadAvailableCurrencies()
    }

    fun loadAccount(accountIdToLoad: Int? = null) {
        Log.d("AccountViewModel", "loadAccount() called with accountIdToLoad: $accountIdToLoad.")
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            // Передаем accountIdToLoad в UseCase. Если null, UseCase возьмет первый.
            getAccountUseCase.execute(accountIdToLoad).onSuccess { account ->
                Log.d("AccountViewModel", "loadAccount() success. Account: ${account.name}, Balance: ${account.balance}, Currency: ${account.currency}, ID: ${account.id}")
                _uiState.update { currentState ->
                    currentState.copy(
                        account = account,
                        editedName = account.name,
                        editedBalance = account.balance,
                        editedCurrency = Currency.fromCode(account.currency) ?: Currency.RUBLE,
                        isLoading = false
                    )
                }
                Log.d("AccountViewModel", "UI State updated after loadAccount success: account=${_uiState.value.account?.name}, editedName=${_uiState.value.editedName}")
                CurrentCurrencyManager.setCurrency(Currency.fromCode(account.currency) ?: Currency.RUBLE)
            }.onError { code, message ->
                Log.e("AccountViewModel", "loadAccount() API error: $code - $message")
                _uiState.update { it.copy(isLoading = false, errorMessage = "Ошибка $code: $message") }
            }.onException { throwable ->
                Log.e("AccountViewModel", "loadAccount() exception: ${throwable.localizedMessage}", throwable)
                val errorMessage = if (throwable is NoSuchElementException) {
                    "Аккаунты не найдены."
                } else {
                    "Ошибка загрузки счета: ${throwable.localizedMessage ?: "Неизвестная ошибка"}"
                }
                _uiState.update { it.copy(isLoading = false, errorMessage = errorMessage) }
            }
        }
    }

    private fun loadAvailableCurrencies() {
        Log.d("AccountViewModel", "loadAvailableCurrencies() called.")
        viewModelScope.launch {
            getAvailableCurrenciesUseCase.execute().onSuccess { currencies ->
                Log.d("AccountViewModel", "Available currencies loaded: ${currencies.map { it.code }}")
                _uiState.update { it.copy(availableCurrencies = currencies) }
            }.onError { code, message ->
                Log.e("AccountViewModel", "loadAvailableCurrencies() API error: $code - $message")
                _uiState.update { it.copy(errorMessage = "Ошибка загрузки валют: $code - $message") }
            }.onException { throwable ->
                Log.e("AccountViewModel", "loadAvailableCurrencies() exception: ${throwable.localizedMessage}", throwable)
                _uiState.update { it.copy(errorMessage = "Ошибка загрузки валют: ${throwable.localizedMessage ?: "Неизвестная ошибка"}") }
            }
        }
    }

    fun onNameChange(newName: String) {
        Log.d("AccountViewModel", "onNameChange: $newName")
        _uiState.update { it.copy(editedName = newName) }
    }

    fun onBalanceChange(newBalance: String) {
        Log.d("AccountViewModel", "onBalanceChange: $newBalance")
        _uiState.update { it.copy(editedBalance = newBalance) }
    }

    fun onCurrencySelected(currency: Currency) {
        Log.d("AccountViewModel", "onCurrencySelected: ${currency.code}")
        _uiState.update { it.copy(editedCurrency = currency, showCurrencySelection = false) }
    }

    fun showCurrencySelection(show: Boolean) {
        Log.d("AccountViewModel", "showCurrencySelection: $show")
        _uiState.update { it.copy(showCurrencySelection = show) }
    }

    fun saveAccount() {
        val currentAccount = _uiState.value.account
        if (currentAccount == null) {
            Log.e("AccountViewModel", "saveAccount() called but currentAccount is null. Cannot save.")
            _uiState.update { it.copy(errorMessage = "Не удалось сохранить: счет не загружен.") }
            return
        }
        Log.d("AccountViewModel", "saveAccount() called. Current edited name: ${_uiState.value.editedName}, balance: ${_uiState.value.editedBalance}, currency: ${_uiState.value.editedCurrency.code}")
        _uiState.update { it.copy(isSaving = true, errorMessage = null, saveSuccess = false) }

        viewModelScope.launch {
            val updatedAccount = currentAccount.copy(
                name = _uiState.value.editedName,
                balance = _uiState.value.editedBalance,
                currency = _uiState.value.editedCurrency.code
            )
            Log.d("AccountViewModel", "Attempting to update account with ID: ${currentAccount.id}, data: $updatedAccount")

            // Используем ID текущего загруженного аккаунта для обновления
            updateAccountUseCase.execute(currentAccount.id, updatedAccount).onSuccess { savedAccount ->
                Log.d("AccountViewModel", "saveAccount() success. Saved Account: ${savedAccount.name}, Balance: ${savedAccount.balance}, Currency: ${savedAccount.currency}, ID: ${savedAccount.id}")
                _uiState.update { currentState ->
                    currentState.copy(
                        account = savedAccount,
                        editedName = savedAccount.name,
                        editedBalance = savedAccount.balance,
                        editedCurrency = Currency.fromCode(savedAccount.currency) ?: Currency.RUBLE,
                        isSaving = false,
                        saveSuccess = true,
                        isEditing = false
                    )
                }
                Log.d("AccountViewModel", "UI State updated after saveAccount success: account=${_uiState.value.account?.name}, editedName=${_uiState.value.editedName}")
                CurrentCurrencyManager.setCurrency(Currency.fromCode(savedAccount.currency) ?: Currency.RUBLE)
            }.onError { code, message ->
                Log.e("AccountViewModel", "saveAccount() API error: $code - $message")
                _uiState.update { it.copy(isSaving = false, errorMessage = "Ошибка сохранения $code: $message") }
            }.onException { throwable ->
                Log.e("AccountViewModel", "saveAccount() exception: ${throwable.localizedMessage}", throwable)
                _uiState.update { it.copy(isSaving = false, errorMessage = "Ошибка сохранения счета: ${throwable.localizedMessage ?: "Неизвестная ошибка"}") }
            }
        }
    }

    fun resetSaveSuccess() {
        Log.d("AccountViewModel", "resetSaveSuccess() called.")
        _uiState.update { it.copy(saveSuccess = false) }
    }
}