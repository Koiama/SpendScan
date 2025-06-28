package com.spendscan.core.domain.models

/**
 * Категория для классификации транзакций
 */

data class Category(
    val id: Int,
    val name: String,
    val emoji: String,
    val isIncome: Boolean
)