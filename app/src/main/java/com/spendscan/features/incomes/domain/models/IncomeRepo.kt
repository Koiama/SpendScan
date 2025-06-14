package com.spendscan.features.incomes.domain.models

import com.spendscan.features.account.domain.models.Currency

data class IncomeRepoData(
    val balance: Double,
    val currency: Currency,
    val income: List<RepoIncome>,
)

data class RepoIncome(
    val id: Int,
    val categoryName: String,
    val amount: Double,
    val comment: String?
)