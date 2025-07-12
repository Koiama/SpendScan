package com.spendscan.spendscan.feature.history.ui.viewmodel.contract

import com.spendscan.spendscan.core.domain.models.account.Currency
import com.spendscan.spendscan.core.domain.models.transaction.Transaction
import java.time.LocalDate

/**
 * Модель состояния UI для экрана истории транзакций.
 * Содержит все данные, необходимые для отображения экрана истории операций
 * @property dateStart Начальная дата периода фильтрации
 * @property dateEnd Конечная дата периода фильтрации
 * @property transactions Список транзакций за период (отсортированный)
 * @property amount Итоговая сумма доходов/расходов
 * @property currency Символ валюты для отображения
 * @property isLoading Флаг активности загрузки:
 *   - true: показать индикатор загрузки
 *   - false: скрыть индикатор
 * @property errorMessage Сообщение об ошибке или null при отсутствии
 */
data class HistoryState(
    val dateStart: LocalDate = LocalDate.now().withDayOfMonth(1),
    val dateEnd: LocalDate =  LocalDate.now(),
    val transactions: List<Transaction> = emptyList(),
    val amount: Double = 0.0,
    val isDatePickerShown: Boolean = false,
    val currency: Currency = Currency.RUB,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
