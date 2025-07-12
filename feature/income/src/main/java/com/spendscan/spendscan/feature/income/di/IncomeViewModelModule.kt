package com.spendscan.spendscan.feature.income.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import com.spendscan.spendscan.core.di.viewmodel.DaggerViewModelFactory
import com.spendscan.spendscan.core.di.viewmodel.ViewModelKey
import com.spendscan.spendscan.feature.income.ui.viewmodel.IncomeViewModel

@Module
interface IncomeViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(IncomeViewModel::class)
    fun bindIncomeViewModel(
        viewModel: IncomeViewModel
    ): ViewModel

    @Binds
    fun bindFactory(
        factory: DaggerViewModelFactory
    ): ViewModelProvider.Factory
}
