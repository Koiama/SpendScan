package com.spendscan.spendscan.feature.income.ui.viewmodel.contract

import com.spendscan.spendscan.core.domain.models.account.Currency
import com.spendscan.spendscan.core.domain.models.transaction.Transaction

/**
 * Состояние UI для экрана доходов
 *
 * @property incomes список доходных транзакций
 * @property amount общая сумма доходов
 * @property currency отображаемый символ валюты
 * @property isLoading флаг загрузки данных (true - идёт загрузка)
 * @property errorMessage текст ошибки (null при отсутствии ошибок)
 */
data class IncomeState(
    val incomes: List<Transaction> = emptyList(),
    val amount: Double = 0.0,
    val currency: Currency = Currency.RUB,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
