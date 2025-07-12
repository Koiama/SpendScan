package com.spendscan.spendscan.core.data.mapper.transaction

import com.spendscan.spendscan.core.data.mapper.toDomainCurrency
import com.spendscan.spendscan.core.data.models.account.AccountBriefResponse
import com.spendscan.spendscan.core.domain.models.account.AccountBrief

fun AccountBriefResponse.toDomain(): AccountBrief = AccountBrief(
    id = this.id,
    name = this.name,
    currency = this.currency.toDomainCurrency(),
    balance = this.balance
)
