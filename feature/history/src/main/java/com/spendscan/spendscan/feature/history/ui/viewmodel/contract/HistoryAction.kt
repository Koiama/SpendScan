package com.spendscan.spendscan.feature.history.ui.viewmodel.contract

import java.time.LocalDate

/**
 * Описывает все возможные пользовательские действия и системные события,
 * которые могут обрабатываться ViewModel экрана истории
 */
sealed interface HistoryAction {
    /**
     * Действие обновления начальной даты периода.
     * @property date Новая начальная дата
     */
    data class UpdateDateStart(val date: LocalDate) : HistoryAction
    /**
     * Действие обновления конечной даты периода.
     * @property date Новая конечная дата
     */
    data class UpdateDateEnd(val date: LocalDate) : HistoryAction
    /**
     * Действие для загрузки транзакций
     */
    data object LoadTransactions : HistoryAction

    data object ChangeDatePickerVisibility: HistoryAction
}
