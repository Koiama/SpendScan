package com.spendscan.spendscan.feature.manage_transaction.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class ManageTransactionViewModelFactory(
    private val assistedFactory: ViewModelAssistedFactory,
    private val isIncomeScreen: Boolean,
    private val transactionId: Long?,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return assistedFactory.create(isIncomeScreen, transactionId) as T
    }
}
