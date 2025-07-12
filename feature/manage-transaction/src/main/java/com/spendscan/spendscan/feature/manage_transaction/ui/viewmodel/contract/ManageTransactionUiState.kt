package com.spendscan.spendscan.feature.manage_transaction.ui.viewmodel.contract

import com.spendscan.spendscan.core.domain.models.account.Currency
import com.spendscan.spendscan.core.domain.models.transaction.Category
import java.time.LocalDateTime

data class ManageTransactionUiState(
    val isLoading: Boolean = false,
    val accountName: String = "Мой счет",
    val accountId: Long = 1,
    val category: Category = Category(
        name = "Ремонт",
        id = -1L,
        emoji = "\u3232",
        isIncome = false
    ),
    val isDropdownMenuExpanded: Boolean = false,
    val isDatePickerShown: Boolean = false,
    val isTimePickerShown: Boolean = false,
    val stringAmount: String = "0.00",
    val currency: Currency = Currency.RUB,
    val dateTime: LocalDateTime = LocalDateTime.now(),
    val description: String = "",
    val categories: List<Category> = emptyList()
)