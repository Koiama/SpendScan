package com.spendscan.core.data.mapper

import com.spendscan.core.data.models.AccountFullDto
import com.spendscan.features.account.domain.models.Account

fun AccountFullDto.toDomain(): Account {
    return Account(
        id = id,
        userId = userId,
        name = name,
        balance = balance,
        currency = currency,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
