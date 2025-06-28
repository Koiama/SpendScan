package com.spendscan.features.account.domain.models

import com.spendscan.features.expenses.myHistory.data.models.AccountState

data class History(
    val id: Int,
    val accountId: Int,
    val changeType: String,
    val previousState: AccountState,
    val newState: AccountState,
    val changeTimestamp: String,
    val createdAt: String
)

data class AccountHistory(
    val accountId: Int,
    val accountName: String,
    val currency: String,
    val currentBalance: String,
    val history: History
)