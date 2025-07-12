package com.spendscan.spendscan.feature.balance.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.spendscan.spendscan.core.common.utils.format.formatMoney
import com.spendscan.spendscan.core.common.utils.onError
import com.spendscan.spendscan.core.common.utils.onException
import com.spendscan.spendscan.core.common.utils.onSuccess
import com.spendscan.spendscan.core.domain.usecase.GetAccountInfoUseCase
import com.spendscan.spendscan.feature.balance.ui.viewmodel.contract.BalanceState
import javax.inject.Inject

/**
 * ViewModel для экрана баланса, управляющая состоянием данных
 */
class BalanceViewModel @Inject constructor(
    private val getAccountInfoUseCase: GetAccountInfoUseCase
) : ViewModel() {
    private var _state = MutableStateFlow<BalanceState>(BalanceState())
    val state = _state.asStateFlow()

    init {
        loadUsersBalance()
    }

    private fun loadUsersBalance() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _state.update { it.copy(isLoading = true) }
                val result = getAccountInfoUseCase()
                result.onSuccess { res ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            amount = formatMoney(res.balance),
                            accountName = res.name,
                            currency = res.currency
                        )
                    }
                }.onError { code, message ->
                    _state.update { it.copy(isLoading = false, error = message) }
                }.onException {
                    Log.d("DEBUG", "loadUsersBalance: $it")
                }
            }
        }
    }
}
