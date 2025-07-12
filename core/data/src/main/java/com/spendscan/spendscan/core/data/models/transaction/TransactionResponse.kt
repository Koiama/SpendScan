package com.spendscan.spendscan.core.data.models.transaction

import com.spendscan.spendscan.core.data.models.account.AccountBriefResponse

/**
 * Полная информация о транзакции с сервера.
 *
 * @property id Уникальный идентификатор
 * @property account Краткая информация о счёте
 * @property category Данные категории транзакции
 * @property amount Сумма транзакции
 * @property transactionDate Дата совершения (ISO 8601)
 * @property comment Комментарий пользователя
 * @property createdAt Дата создания записи
 * @property updatedAt Дата последнего обновления
 */
data class TransactionResponse(
    val id: Long,
    val account: AccountBriefResponse,
    val category: CategoryResponse,
    val amount: Double,
    val transactionDate: String,
    val comment: String?,
    val createdAt: String,
    val updatedAt: String
)
