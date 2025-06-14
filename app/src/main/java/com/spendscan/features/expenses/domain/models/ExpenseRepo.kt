package com.spendscan.features.expenses.domain.models

import com.spendscan.features.account.domain.models.Currency

data class ExpensesRepoData(
    val balance: Double,
    val currency: Currency,
    val expenses: List<RepoExpense>,
)

data class RepoExpense(
    val id: Int,
    val categoryName: String,
    val categoryEmoji: String,
    val amount: Double,
    val comment: String?
)
