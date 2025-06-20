package com.spendscan.features.incomes.myHistory.data.models

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class TransactionDto (
    val id: Int,
    val account: AccountState,
    val category: Category,
    val amount: String,
    val transactionDate: String,
    val comment: String? = null,
    val createdAt: String,
    val updatedAt: String
)