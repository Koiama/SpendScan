package com.spendscan.spendscan.core.di.modules

import dagger.Module
import dagger.Provides
import com.spendscan.spendscan.core.common.utils.AppScope
import com.spendscan.spendscan.core.domain.repository.AccountRepository
import com.spendscan.spendscan.core.domain.usecase.GetAccountInfoUseCase

@Module
class UseCasesModule {

    @Provides
    @AppScope
    fun provideGetAccountInfoUseCase(
        accountRepository: AccountRepository
    ): GetAccountInfoUseCase = GetAccountInfoUseCase(accountRepository)
}
