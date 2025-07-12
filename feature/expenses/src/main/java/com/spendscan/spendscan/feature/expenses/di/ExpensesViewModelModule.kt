package com.spendscan.spendscan.feature.expenses.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import com.spendscan.spendscan.core.di.viewmodel.DaggerViewModelFactory
import com.spendscan.spendscan.core.di.viewmodel.ViewModelKey
import com.spendscan.spendscan.feature.expenses.ui.viewmodel.ExpensesViewModel

@Module
interface ExpensesViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ExpensesViewModel::class)
    fun bindExpensesViewModel(
        viewModel: ExpensesViewModel
    ): ViewModel

    @Binds
    fun bindFactory(
        factory: DaggerViewModelFactory
    ): ViewModelProvider.Factory
}
