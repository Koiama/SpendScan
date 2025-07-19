package com.spendscan.spendscan.core.domain.models.transaction

import com.spendscan.spendscan.core.domain.models.account.AccountBrief
import java.time.LocalDateTime

/**
 * Финансовая транзакция
 */
data class Transaction(
    val id: Long,
    val account: AccountBrief,
    val category: Category,
    val amount: Double,
    val date: LocalDateTime,
    val comment: String?
)

