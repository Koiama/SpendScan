package com.spendscan.spendscan.core.domain.repository

import com.spendscan.spendscan.core.common.utils.Result
import com.spendscan.spendscan.core.domain.models.account.AccountBrief
import com.spendscan.spendscan.core.domain.models.account.AccountUpdateInfo

interface AccountRepository {
    suspend fun getAccountInfo(): Result<AccountBrief>
    suspend fun updateAccountInfo(accountId: Long, info: AccountUpdateInfo): Result<AccountBrief>

}