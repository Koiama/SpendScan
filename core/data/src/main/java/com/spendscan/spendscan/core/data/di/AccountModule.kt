package com.spendscan.spendscan.core.data.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import com.spendscan.spendscan.core.common.utils.AppScope
import com.spendscan.spendscan.core.data.api.AccountApi

@Module
class AccountModule {

    @AppScope
    @Provides
    fun provideAccountApi(retrofit: Retrofit): AccountApi = retrofit.create(AccountApi::class.java)

}