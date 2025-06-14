package com.spendscan.features.incomes.domain.models

data class Income(
    val id: Int,
    val title: String,
    val amount: String,
    val subtitle: String?
)