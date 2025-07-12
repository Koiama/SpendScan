package com.spendscan.spendscan.feature.income.ui.viewmodel.contract

sealed interface IncomeAction {
    data object Refresh: IncomeAction
}