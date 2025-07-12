package com.spendscan.spendscan.feature.income.di

import androidx.lifecycle.ViewModelProvider
import dagger.Component
import com.spendscan.spendscan.core.di.component.CoreComponentDeps

@IncomeScope
@Component(
    dependencies = [CoreComponentDeps::class],
    modules = [
        IncomeViewModelModule::class
    ]
)
interface IncomeComponent {
    fun viewModelFactory(): ViewModelProvider.Factory

    @Component.Factory
    interface Factory {
        fun create(
            deps: CoreComponentDeps
        ): IncomeComponent
    }
}
