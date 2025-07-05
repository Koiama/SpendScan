package com.spendscan.features.incomes.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.spendscan.core.domain.repository.TransactionRepository
import com.spendscan.core.network.ConnectivityObserver
import com.spendscan.features.incomes.useCase.GetTodayIncomeUseCase

class TodayIncomeViewModelFactory(
    private val getTodayIncomeUseCase: GetTodayIncomeUseCase,
    private val connectivityObserver: ConnectivityObserver
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodayIncomeViewModel::class.java)) {
            return TodayIncomeViewModel(
                getTodayIncomeUseCase = getTodayIncomeUseCase,
                connectivityObserver = connectivityObserver
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}