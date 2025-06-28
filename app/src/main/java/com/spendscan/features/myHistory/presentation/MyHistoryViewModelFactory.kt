package com.spendscan.features.myHistory.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.spendscan.core.domain.repository.TransactionRepository
import com.spendscan.core.network.ConnectivityObserver
import com.spendscan.features.myHistory.useCase.GetTransactionsByTypeUseCase

// TransactionHistoryViewModelFactory: Фабрика для создания TransactionHistoryViewModel.
class MyHistoryViewModelFactory(
    private val repository: TransactionRepository,
    private val accountId: Int,
    private val isIncome: Boolean? = null,
    private val connectivityObserver: ConnectivityObserver
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyHistoryViewModel::class.java)) {
            val getTransactionsByTypeUseCase = GetTransactionsByTypeUseCase(repository)
            return MyHistoryViewModel(
                getTransactionsByTypeUseCase,
                accountId,
                isIncome,
                connectivityObserver
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}