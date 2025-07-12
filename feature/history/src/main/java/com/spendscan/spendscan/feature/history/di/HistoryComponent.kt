package com.spendscan.spendscan.feature.history.di

import androidx.lifecycle.ViewModelProvider
import dagger.Component
import com.spendscan.spendscan.core.di.component.CoreComponentDeps

@HistoryScope
@Component(
    dependencies = [CoreComponentDeps::class],
    modules = [
        HistoryViewModelModule::class
    ]
)
interface HistoryComponent {
    fun viewModelFactory(): ViewModelProvider.Factory

    @Component.Factory
    interface Factory {
        fun create(
            deps: CoreComponentDeps
        ): HistoryComponent
    }
}
