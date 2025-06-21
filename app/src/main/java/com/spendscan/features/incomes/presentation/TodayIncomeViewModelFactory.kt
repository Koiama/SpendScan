package com.spendscan.features.incomes.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.spendscan.features.expenses.myHistory.domain.MyHistoryRepository

// TodayIncomeViewModelFactory: Фабрика для создания TodayIncomeViewModel.
// Передает репозиторий и accountId в ViewModel.
class TodayIncomeViewModelFactory(
    private val repository: MyHistoryRepository,
    private val accountId: String
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodayIncomeViewModel::class.java)) {
            return TodayIncomeViewModel(repository, accountId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}