package com.spendscan.spendscan.feature.balance.ui.viewmodel.contract

sealed interface BalanceEffect {
    data class ShowError(val message: String) : BalanceEffect
}