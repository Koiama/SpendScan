package com.spendscan.spendscan.feature.balance.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import com.spendscan.spendscan.core.di.viewmodel.DaggerViewModelFactory
import com.spendscan.spendscan.core.di.viewmodel.ViewModelKey
import com.spendscan.spendscan.feature.balance.ui.viewmodel.BalanceViewModel

@Module
interface BalanceViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(BalanceViewModel::class)
    fun bindBalanceViewModel(
        viewModel: BalanceViewModel
    ): ViewModel

    @Binds
    fun bindFactory(
        factory: DaggerViewModelFactory
    ): ViewModelProvider.Factory
}
