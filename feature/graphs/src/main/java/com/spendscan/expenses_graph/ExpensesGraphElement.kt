package com.spendscan.expenses_graph

import java.time.LocalDate

data class ExpensesGraphElement(
    val date: LocalDate,
    val amount: Float,
    val isPositive: Boolean
)
