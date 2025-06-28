package com.spendscan.features.expenses.presentation

import ExpensesViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.spendscan.core.network.ConnectivityObserver
import com.spendscan.features.expenses.useCase.GetTodayExpensesUseCase

class ExpensesViewModelFactory(
    private val getTodayExpensesUseCase: GetTodayExpensesUseCase,
    private val accountId: Int,
    private val connectivityObserver: ConnectivityObserver
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpensesViewModel::class.java)) {
            return ExpensesViewModel(
                getTodayExpensesUseCase = getTodayExpensesUseCase,
                accountId = accountId,
                connectivityObserver = connectivityObserver
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}