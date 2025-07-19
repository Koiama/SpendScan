package com.spendscan.spendscan.core.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey
    val id: Long,
    val accountId: Long,
    val categoryId: Long,
    val amount: Double,
    val transactionDate: LocalDateTime,
    val comment: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
    val currencyCode: String
)