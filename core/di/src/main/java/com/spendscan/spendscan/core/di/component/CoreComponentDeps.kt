package com.spendscan.spendscan.core.di.component

import retrofit2.Retrofit
import com.spendscan.spendscan.core.domain.repository.AccountRepository
import com.spendscan.spendscan.core.domain.repository.TransactionRepository
import com.spendscan.spendscan.core.domain.usecase.GetAccountInfoUseCase

interface CoreComponentDeps {
    fun accountRepository(): AccountRepository
    fun transactionRepository(): TransactionRepository
    fun getAccountInfoUseCase(): GetAccountInfoUseCase
    fun retrofit(): Retrofit
}
