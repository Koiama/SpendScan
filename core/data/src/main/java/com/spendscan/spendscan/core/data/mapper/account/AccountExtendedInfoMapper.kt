package com.spendscan.spendscan.core.data.mapper.account

import com.spendscan.spendscan.core.data.mapper.toDomainCurrency
import com.spendscan.spendscan.core.data.models.account.AccountExtendedInfoResponse
import com.spendscan.spendscan.core.domain.models.account.AccountBrief

fun AccountExtendedInfoResponse.toDomain() = AccountBrief(
    id = this.id,
    name = this.name,
    currency = this.currency.toDomainCurrency(),
    balance = this.balance
)