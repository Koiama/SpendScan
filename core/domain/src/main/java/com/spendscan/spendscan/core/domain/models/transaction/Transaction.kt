package com.spendscan.spendscan.core.domain.models.transaction

import com.spendscan.spendscan.core.domain.models.account.AccountBrief
import java.time.LocalDateTime

/**
 * Финансовая транзакция
 *
 * @property id Уникальный идентификатор транзакции
 * @property account Связанный счёт
 * @property category Категория транзакции
 * @property amount Сумма операции
 * @property date Дата и время совершения транзакции
 * @property comment Дополнительный комментарий
 */
data class Transaction(
    val id: Long,
    val account: AccountBrief,
    val category: Category,
    val amount: Double,
    val date: LocalDateTime,
    val comment: String?
)

