package com.spendscan.features.incomes.myHistory.data.models

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class TransactionAccount(
    val id: String,
    val name: String,
    val balance: String,
    val currency: String,
    val emoji: String?= null,
    val isDefault: Boolean? = null
)