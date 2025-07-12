package com.spendscan.spendscan.feature.manage_transaction.di.viewmodel

import dagger.assisted.AssistedFactory
import com.spendscan.spendscan.feature.manage_transaction.ui.viewmodel.ManageTransactionViewModel

@AssistedFactory
interface ViewModelAssistedFactory {
    fun create(isIncomeScreen: Boolean, transactionId: Long?): ManageTransactionViewModel
}