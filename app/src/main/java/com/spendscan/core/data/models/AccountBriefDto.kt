package com.spendscan.core.data.models

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

/**
 * Краткая информация о аккаунте от API
 */

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class AccountBriefDto(
    val id: Int,
    val name: String,
    val balance: String,
    val currency: String
)