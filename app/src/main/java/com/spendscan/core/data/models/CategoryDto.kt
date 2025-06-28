package com.spendscan.core.data.models

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

/**
 * Категория для классификации транзакций от API
 */

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class CategoryDto(
    val id: Int,
    val name: String,
    val emoji: String,
    val isIncome: Boolean
    )