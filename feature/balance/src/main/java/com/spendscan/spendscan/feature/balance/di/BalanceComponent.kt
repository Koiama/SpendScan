package com.spendscan.spendscan.feature.balance.di

import androidx.lifecycle.ViewModelProvider
import dagger.Component
import com.spendscan.spendscan.core.di.component.CoreComponentDeps

@BalanceScope
@Component(
    dependencies = [CoreComponentDeps::class],
    modules = [
        BalanceViewModelModule::class
    ]
)
interface BalanceComponent {
    fun viewModelFactory(): ViewModelProvider.Factory

    @Component.Factory
    interface Factory {
        fun create(
            deps: CoreComponentDeps
        ): BalanceComponent
    }
}
