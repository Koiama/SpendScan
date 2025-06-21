package com.spendscan.features.expenses.myHistory.data.models

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class AccountState(
    val id: Int,
    val name: String,
    val balance: String,
    val currency: String
)