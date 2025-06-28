package com.spendscan.core.data.mapper

import com.spendscan.core.data.models.AccountBriefDto
import com.spendscan.core.domain.models.AccountBrief

fun AccountBriefDto.toDomain(): AccountBrief {
    return AccountBrief(
        id = id,
        name = name,
        currency =currency,
        balance = balance
    )
}