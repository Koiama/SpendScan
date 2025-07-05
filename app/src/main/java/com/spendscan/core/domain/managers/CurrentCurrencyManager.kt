package com.spendscan.core.domain.managers

import com.spendscan.features.account.domain.models.Currency
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object CurrentCurrencyManager {
    private val _currentCurrency = MutableStateFlow(Currency.RUBLE)
    val currentCurrency: StateFlow<Currency> = _currentCurrency.asStateFlow()

    fun setCurrency(currency: Currency) {
        _currentCurrency.value = currency
    }

    fun getCurrentCurrency(): Currency {
        return _currentCurrency.value
    }
}