package com.spendscan.data.models

data class Expenses(
    val id: String,
    val title: String,
    val subtitle: String?,
    val createdAt: String,
    val trailTag: String,
    val trailText: String,
    val iconTag: String,
)

enum class ExpensesType(val displayName: String) {
    FOOD("Еда"),
    TRANSPORT("Транспорт"),
    HOUSING("Жилье"),
    ENTERTAINMENT("Развлечения"),
    OTHER("Прочее")
}

data class ExpensesDetailed(
    val id: String,
    val createdAt: String,
    val description: String,
    val sum: String,
    val dateText: String,
    val checkId: String,
    val expensesType: ExpensesType,
)