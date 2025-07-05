package com.spendscan.core.data.models

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable


// DTO для получения/отправки полной информации о счете
@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class AccountFullDto(
    val id: Int,
    val userId: Int,
    val name: String,
    val balance: String,
    val currency: String,
    val createdAt: String,
    val updatedAt: String
)