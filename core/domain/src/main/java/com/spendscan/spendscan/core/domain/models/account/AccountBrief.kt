package com.spendscan.spendscan.core.domain.models.account

/**
 * Информация о банковском счёте
 *
 * @property id Уникальный идентификатор счёта
 * @property name Название счёта
 * @property currency Валюта счёта (RUB, EUR)
 * @property balance Текущий баланс в виде строки
 */
data class AccountBrief(
    val id: Long,
    val name: String,
    val currency: Currency,
    val balance: Double
) {
    companion object {
        fun empty() = AccountBrief(
            id = 0L,
            name = "",
            currency = Currency.RUB,
            balance = 0.0
        )
    }
}

enum class Currency(val symbol: String) {
    RUB("\u20BD"),
    USD("\u0024"),
    EUR("\u20AC")
}