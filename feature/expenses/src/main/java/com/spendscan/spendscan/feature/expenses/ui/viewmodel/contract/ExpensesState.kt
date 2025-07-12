package com.spendscan.spendscan.feature.expenses.ui.viewmodel.contract
import com.spendscan.spendscan.core.domain.models.account.Currency
import com.spendscan.spendscan.core.domain.models.transaction.Transaction

/**
 * Модель состояния UI для экрана расходов.
 * @property expenses Список расходных транзакций
 * @property amount Общая сумма расходов
 * @property currency Символ валюты
 * @property isLoading Флаг активности загрузки данных:
 * `true`: показать индикатор загрузки
 * `false`: скрыть индикатор
 * @property errorMessage Сообщение об ошибке или `null` при отсутствии ошибок
 */
data class ExpensesState(
    val expenses: List<Transaction> = emptyList(),
    val amount: Double = 0.0,
    val currency: Currency = Currency.RUB,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
