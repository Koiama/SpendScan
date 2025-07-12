package com.spendscan.spendscan.core.data.models.account

/**
 * Информация о банковском счёте
 *
 * @property id Уникальный идентификатор счёта
 * @property name Название счёта
 * @property balance Текущий баланс в виде строки
 * @property currency Валюта счёта (RUB, USD)
 */
data class AccountBriefResponse(
    val id: Long,
    val name: String,
    val balance: Double,
    val currency: String
)