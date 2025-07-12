package com.spendscan.spendscan.feature.edit_balance.domain.usecase

import com.spendscan.spendscan.core.common.utils.Result
import com.spendscan.spendscan.core.domain.models.account.AccountBrief
import com.spendscan.spendscan.core.domain.models.account.AccountUpdateInfo
import com.spendscan.spendscan.core.domain.repository.AccountRepository
import javax.inject.Inject

class UpdateAccountByIdUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(accountId: Long, accountInfo: AccountUpdateInfo): Result<AccountBrief> {
        return accountRepository.updateAccountInfo(accountId, accountInfo)
    }
}