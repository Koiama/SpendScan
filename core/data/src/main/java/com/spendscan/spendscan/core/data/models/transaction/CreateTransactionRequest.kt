package com.spendscan.spendscan.core.data.models.transaction

import com.spendscan.spendscan.core.common.utils.format.toUtcIsoString
import java.time.LocalDateTime

data class CreateTransactionRequest(
    val accountId: Long,
    val categoryId: Long,
    val amount: String,
    val transactionDate: String,
    val comment: String
) {
    companion object {
        fun create(
            accountId: Long,
            categoryId: Long,
            amount: String,
            date: LocalDateTime,
            comment: String,
        ) = CreateTransactionRequest(
            accountId = accountId,
            categoryId = categoryId,
            amount = amount,
            transactionDate = date.toUtcIsoString(),
            comment = comment,
        )
    }
}