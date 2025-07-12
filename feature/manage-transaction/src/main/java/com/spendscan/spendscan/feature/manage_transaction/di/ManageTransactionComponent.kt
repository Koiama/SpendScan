package com.spendscan.spendscan.feature.manage_transaction.di

import dagger.Component
import com.spendscan.spendscan.core.di.component.CoreComponentDeps
import com.spendscan.spendscan.feature.categories.di.CategoriesComponentDeps
import com.spendscan.spendscan.feature.manage_transaction.di.viewmodel.ViewModelAssistedFactory

@ManageTransactionScope
@Component(
    dependencies = [
        CategoriesComponentDeps::class, CoreComponentDeps::class,
    ]
)
interface ManageTransactionComponent {

    fun manageTransactionViewModelAssistedFactory(): ViewModelAssistedFactory

    @Component.Factory
    interface Factory {
        fun create(
            deps: CoreComponentDeps,
            categoriesDeps: CategoriesComponentDeps
        ): ManageTransactionComponent
    }
}
