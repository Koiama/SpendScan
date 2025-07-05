package com.spendscan.features.account.domain

import com.spendscan.features.account.domain.models.Account
import com.spendscan.core.common.Result

interface AccountRepository {
    suspend fun getAccounts(): Result<List<Account>>
    suspend fun updateAccount(accountId: Int, account: Account): Result<Account>
}