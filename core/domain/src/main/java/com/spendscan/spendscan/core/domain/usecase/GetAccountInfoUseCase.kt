package com.spendscan.spendscan.core.domain.usecase

import com.spendscan.spendscan.core.common.utils.Result
import com.spendscan.spendscan.core.domain.models.account.AccountBrief
import com.spendscan.spendscan.core.domain.repository.AccountRepository

class GetAccountInfoUseCase(
    private val accountRepository: AccountRepository
){
    suspend operator fun invoke(): Result<AccountBrief> = accountRepository.getAccountInfo()
}