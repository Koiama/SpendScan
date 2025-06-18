package com.spendscan.features.incomes.myHistory.data.models

data class TransactionDto (
    val id: String,
    val amount: String,
    val currency: String,
    val account: TransactionAccount,
    val emoji: String? = null,
    val transactionDate: String,
    val createdAt: String,
    val updatedAt: String
)