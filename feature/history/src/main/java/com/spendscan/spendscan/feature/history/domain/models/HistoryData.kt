package com.spendscan.spendscan.feature.history.domain.models

import com.spendscan.spendscan.core.domain.models.account.Currency
import com.spendscan.spendscan.core.domain.models.transaction.Transaction

/**
 * Модель данных, представляющая историю транзакций за определенный период
 *
 * @property transactions Список транзакций за период
 * @property amount Итоговый финансовый результат
 * @property currency Символ валюты для форматирования
 */
data class HistoryData(
    val transactions: List<Transaction> = emptyList(),
    val amount: Double = 0.0,
    val currency: Currency = Currency.RUB
)
