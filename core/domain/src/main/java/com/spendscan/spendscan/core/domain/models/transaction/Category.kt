package com.spendscan.spendscan.core.domain.models.transaction

/**
 * Категория для классификации транзакций
 *
 * @property id Уникальный идентификатор категории
 * @property name Название категории
 * @property emoji Визуальный идентификатор категории
 * @property isIncome Флаг, определяющий тип категории:
 *   - true: категория для доходов
 *   - false: категория для расходов
 */
data class Category(
    val id: Long,
    val name: String,
    val emoji: String,
    val isIncome: Boolean
)
