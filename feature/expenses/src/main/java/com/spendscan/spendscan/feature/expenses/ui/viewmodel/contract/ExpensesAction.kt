package com.spendscan.spendscan.feature.expenses.ui.viewmodel.contract

sealed interface ExpensesAction {
    data object Refresh: ExpensesAction
}