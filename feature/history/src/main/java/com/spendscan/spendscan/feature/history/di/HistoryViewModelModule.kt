package com.spendscan.spendscan.feature.history.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import com.spendscan.spendscan.core.di.viewmodel.DaggerViewModelFactory
import com.spendscan.spendscan.core.di.viewmodel.ViewModelKey
import com.spendscan.spendscan.feature.history.ui.viewmodel.HistoryViewModel

@Module
interface HistoryViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(HistoryViewModel::class)
    fun bindHistoryViewModel(
        viewModel: HistoryViewModel
    ): ViewModel

    @Binds
    fun bindFactory(
        factory: DaggerViewModelFactory
    ): ViewModelProvider.Factory
}
