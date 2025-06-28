package com.spendscan.core.data.models

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

/**
 * Информация о транзакции от API
 */

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class TransactionDto (
    val id: Int,
    val account: AccountBriefDto,
    val category: CategoryDto,
    val amount: String,
    val transactionDate: String,
    val comment: String? = null,
    val createdAt: String,
    val updatedAt: String
)