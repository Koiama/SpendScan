package com.spendscan.core.data.mapper

import com.spendscan.core.data.models.AccountBriefDto
import com.spendscan.core.domain.models.AccountBrief
import com.spendscan.features.account.domain.models.Account

fun AccountBriefDto.toDomain(): AccountBrief {
    return AccountBrief(
        id = id,
        name = name,
        currency =currency,
        balance = balance
    )
}

fun Account.toBriefDto(): AccountBriefDto {
    return AccountBriefDto(
        id = id,
        name = name,
        balance = balance,
        currency = currency
    )
}