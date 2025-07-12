package com.spendscan

import android.app.Application
import com.spendscan.spendscan.core.di.component.CoreComponent
import com.spendscan.spendscan.core.di.component.CoreComponentProvider
import com.spendscan.spendscan.core.di.component.DaggerCoreComponent

class SpendScanApplication: Application(), CoreComponentProvider {

    override lateinit var coreComponent: CoreComponent
        private set

    override fun onCreate() {
        super.onCreate()

        coreComponent = DaggerCoreComponent.factory()
            .create(BuildConfig.API_KEY)
    }
}