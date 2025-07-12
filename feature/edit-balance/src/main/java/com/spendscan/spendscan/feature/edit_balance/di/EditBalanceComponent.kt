package com.spendscan.spendscan.feature.edit_balance.di

import androidx.lifecycle.ViewModelProvider
import dagger.Component
import com.spendscan.spendscan.core.di.component.CoreComponentDeps

@EditBalanceScope
@Component(
    dependencies = [CoreComponentDeps::class],
    modules = [
        EditBalanceViewModelModule::class
    ]
)
interface EditBalanceComponent {
    fun viewModelFactory(): ViewModelProvider.Factory

    @Component.Factory
    interface Factory {
        fun create(
            deps: CoreComponentDeps
        ): EditBalanceComponent
    }
}
