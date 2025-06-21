package com.spendscan.features.expenses.myHistory.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.spendscan.features.expenses.myHistory.domain.MyHistoryRepository
import com.spendscan.features.expenses.myHistory.data.ConnectivityObserver

class MyHistoryViewModelFactory(
    private val repository: MyHistoryRepository,
    private val accountId: String,
    private val connectivityObserver: ConnectivityObserver // <-- Добавляем сюда
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) : T{
        if (modelClass.isAssignableFrom(MyHistoryViewModel::class.java)){
            return MyHistoryViewModel(repository, accountId, connectivityObserver) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}