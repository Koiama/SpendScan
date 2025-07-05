package com.spendscan.core.domain.managers

import android.util.Log
import com.spendscan.core.common.onError
import com.spendscan.core.common.onException
import com.spendscan.core.common.onSuccess
import com.spendscan.core.data.repository.AccountRepositoryImpl
import com.spendscan.core.network.RetrofitClient
import com.spendscan.features.account.domain.GetAccountUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CurrentAccountManager(
    private val getAccountUseCase: GetAccountUseCase
) {
    private val _currentAccountId = MutableStateFlow<Int?>(null)
    val currentAccountId: StateFlow<Int?> = _currentAccountId.asStateFlow()

    private val _currentAccountName = MutableStateFlow<String?>("Мой счёт")
    val currentAccountName: StateFlow<String?> = _currentAccountName.asStateFlow()

    private val _currentAccountCurrency = MutableStateFlow<String?>(null)
    val currentAccountCurrency: StateFlow<String?> = _currentAccountCurrency.asStateFlow()

    private val _isAccountLoading = MutableStateFlow(false)
    val isAccountLoading: StateFlow<Boolean> = _isAccountLoading.asStateFlow()

    private val _accountLoadError = MutableStateFlow<String?>(null)
    val accountLoadError: StateFlow<String?> = _accountLoadError.asStateFlow()

    // Функция для инициализации: должна быть вызвана при запуске приложения
    suspend fun loadDefaultAccount() {
        if (_currentAccountId.value != null) {
            Log.d("CurrentAccountManager", "Default account already loaded: ID=${_currentAccountId.value}")
            return // Аккаунт уже загружен
        }

        Log.d("CurrentAccountManager", "Loading default account...")
        _isAccountLoading.value = true
        _accountLoadError.value = null

        getAccountUseCase.execute(null) // Передаем null, чтобы получить первый аккаунт
            .onSuccess { account ->
                _currentAccountId.update { account.id }
                _currentAccountName.update { account.name.takeIf { it.isNotEmpty() } ?: "Мой счёт" }
                _currentAccountCurrency.update { account.currency }
                _isAccountLoading.value = false
                Log.d("CurrentAccountManager", "Default account loaded: ID=${account.id}, Name=${account.name}, Currency=${account.currency}")
            }
            .onError { code, message ->
                _isAccountLoading.value = false
                _accountLoadError.update { "Ошибка $code: $message" }
                Log.e("CurrentAccountManager", "Error loading default account API: $code - $message")
            }
            .onException { throwable ->
                _isAccountLoading.value = false
                _accountLoadError.update { throwable.localizedMessage ?: "Неизвестная ошибка загрузки аккаунта" }
                if (throwable is NoSuchElementException) {
                    _accountLoadError.update { "Аккаунты не найдены." }
                }
                Log.e("CurrentAccountManager", "Exception loading default account: ${throwable.localizedMessage}", throwable)
            }
    }

}

// Singleton-инстанс менеджера
object GlobalCurrentAccountManager {
    val instance: CurrentAccountManager by lazy {
        CurrentAccountManager(
            getAccountUseCase = GetAccountUseCase(
                repository = AccountRepositoryImpl(
                    accountApiService = RetrofitClient.accountApiService
                )
            )
        )
    }
}