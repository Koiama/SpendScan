package com.spendscan.spendscan.feature.categories.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import com.spendscan.spendscan.core.di.viewmodel.DaggerViewModelFactory
import com.spendscan.spendscan.core.di.viewmodel.ViewModelKey
import com.spendscan.spendscan.feature.categories.ui.viewmodel.CategoriesViewModel

@Module
interface CategoriesViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(CategoriesViewModel::class)
    fun bindCategoriesViewModel(viewModel: CategoriesViewModel): ViewModel

    @Binds
    fun bindFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory
}
