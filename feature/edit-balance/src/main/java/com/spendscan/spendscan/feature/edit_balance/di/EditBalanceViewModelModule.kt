package com.spendscan.spendscan.feature.edit_balance.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import com.spendscan.spendscan.core.di.viewmodel.DaggerViewModelFactory
import com.spendscan.spendscan.core.di.viewmodel.ViewModelKey
import com.spendscan.spendscan.feature.edit_balance.ui.viewmodel.EditBalanceViewModel

@Module
interface EditBalanceViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(EditBalanceViewModel::class)
    fun bindCategoriesViewModel(viewModel: EditBalanceViewModel): ViewModel

    @Binds
    fun bindFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory
}
