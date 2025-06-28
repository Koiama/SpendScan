package com.spendscan.features.expenses.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.spendscan.features.expenses.myHistory.domain.MyHistoryRepository

// TodayExpensesViewModelFactory: Фабрика для создания TodayExpensesViewModel.
// Передает репозиторий и accountId в ViewModel.
class TodayExpensesViewModelFactory(
    private val repository: MyHistoryRepository,
    private val accountId: String
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodayExpensesViewModel::class.java)) {
            return TodayExpensesViewModel(repository, accountId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}