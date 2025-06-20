package com.spendscan.features.incomes.myHistory.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.spendscan.features.incomes.myHistory.domain.MyHistoryRepository

class MyHistoryViewModelFactory(
    private val repository: MyHistoryRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) : T{
        if (modelClass.isAssignableFrom(MyHistoryViewModel::class.java)){
            return MyHistoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}