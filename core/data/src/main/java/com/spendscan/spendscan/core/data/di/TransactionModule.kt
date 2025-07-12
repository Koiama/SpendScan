package com.spendscan.spendscan.core.data.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import com.spendscan.spendscan.core.common.utils.qualifiers.AppScope
import com.spendscan.spendscan.core.data.api.TransactionApi


@Module
class TransactionModule {

    @AppScope
    @Provides
    fun provideTransactionsApi(
        retrofit: Retrofit
    ): TransactionApi = retrofit.create(TransactionApi::class.java)

}
