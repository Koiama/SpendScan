package com.spendscan.features.account.domain.models

import com.spendscan.core.data.models.AccountBriefDto

data class History(
    val id: Int,
    val accountId: Int,
    val changeType: String,
    val previousState: AccountBriefDto,
    val newState: AccountBriefDto,
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