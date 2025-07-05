package com.spendscan.features.account.presentation

import com.spendscan.features.account.domain.models.Account
import com.spendscan.features.account.domain.models.Currency

data class AccountUiState(
    val account: Account? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isEditing: Boolean = false,
    val editedName: String = "",
    val editedBalance: String = "",
    val editedCurrency: Currency = Currency.RUBLE,
    val availableCurrencies: List<Currency> = emptyList(),
    val showCurrencySelection: Boolean = false,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false
)