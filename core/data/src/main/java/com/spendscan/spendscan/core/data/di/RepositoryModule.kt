package com.spendscan.spendscan.core.data.di

import dagger.Binds
import dagger.Module
import com.spendscan.spendscan.core.common.utils.qualifiers.AppScope
import com.spendscan.spendscan.core.data.repository.AccountRepositoryImpl
import com.spendscan.spendscan.core.data.repository.TransactionRepositoryImpl
import com.spendscan.spendscan.core.domain.repository.AccountRepository
import com.spendscan.spendscan.core.domain.repository.TransactionRepository

@Module
interface RepositoryModule {

    @AppScope
    @Binds
    fun bindAccountRepositoryImpl(accountRepositoryImpl: AccountRepositoryImpl): AccountRepository

    @AppScope
    @Binds
    fun bindTransactionRepositoryImpl(transactionRepositoryImpl: TransactionRepositoryImpl): TransactionRepository
}