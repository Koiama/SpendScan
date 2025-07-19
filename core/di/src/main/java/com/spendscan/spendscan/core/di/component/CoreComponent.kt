package com.spendscan.spendscan.core.di.component

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import com.spendscan.spendscan.core.common.utils.AppScope
import com.spendscan.spendscan.core.data.di.AccountModule
import com.spendscan.spendscan.core.data.di.DatabaseModule
import com.spendscan.spendscan.core.data.di.RepositoryModule
import com.spendscan.spendscan.core.data.di.TransactionModule
import com.spendscan.spendscan.core.di.modules.UseCasesModule
import com.spendscan.spendscan.core.network.di.NetworkModule

@AppScope
@Component(
    modules = [
        NetworkModule::class,
        AccountModule::class,
        TransactionModule::class,
        RepositoryModule::class,
        UseCasesModule::class,
        DatabaseModule::class
    ]
)
interface CoreComponent : CoreComponentDeps {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            @BindsInstance apiKey: String
        ): CoreComponent
    }
}
