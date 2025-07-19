package com.spendscan.spendscan.core.domain.models.transaction

/**
 * Категория для классификации транзакций
 */
data class Category(
    val id: Long,
    val name: String,
    val emoji: String,
    val isIncome: Boolean
)
