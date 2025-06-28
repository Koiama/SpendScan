package com.spendscan.core.domain.models

import java.time.LocalDateTime

/**
 * Доменная модель транзакции, представляющая бизнес-сущность.
 */

data class Transaction(
    val id: Int,
    val account: AccountBrief,
    val category: Category,
    val amount: Double,
    val date: LocalDateTime,
    val comment: String?
)
