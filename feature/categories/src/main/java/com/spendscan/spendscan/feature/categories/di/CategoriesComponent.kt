package com.spendscan.spendscan.feature.categories.di

import androidx.lifecycle.ViewModelProvider
import dagger.Component
import com.spendscan.spendscan.core.di.component.CoreComponentDeps
import com.spendscan.spendscan.feature.categories.data.di.CategoriesNetworkModule
import com.spendscan.spendscan.feature.categories.data.di.CategoriesRepositoryModule

@CategoriesScope
@Component(
    dependencies = [CoreComponentDeps::class],
    modules = [
        CategoriesNetworkModule::class,
        CategoriesRepositoryModule::class,
        CategoriesViewModelModule::class
    ]
)
interface CategoriesComponent: CategoriesComponentDeps {
    fun viewModelFactory(): ViewModelProvider.Factory

    @Component.Factory
    interface Factory {
        fun create(
            deps: CoreComponentDeps
        ): CategoriesComponent
    }
}
