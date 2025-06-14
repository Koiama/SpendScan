package com.spendscan.features.expenses.domain.models

import com.spendscan.features.account.domain.models.Currency

data class Expense(
    val id: Int,
    val title: String,
    val iconTag: String,
    val amount: String,
    val subtitle: String?
)

