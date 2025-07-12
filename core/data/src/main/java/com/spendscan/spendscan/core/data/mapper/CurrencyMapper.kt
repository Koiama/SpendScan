package com.spendscan.spendscan.core.data.mapper

import com.spendscan.spendscan.core.domain.models.account.Currency

fun String?.toDomainCurrency(): Currency =
    Currency.entries.firstOrNull { it.name == this?.uppercase() } ?: Currency.RUB