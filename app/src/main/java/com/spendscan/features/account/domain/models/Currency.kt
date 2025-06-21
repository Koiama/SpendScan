package com.spendscan.features.account.domain.models

enum class Currency(val code: String, val symbol: String) { // Добавили 'code'
    RUBLE("RUB", "₽"),
    DOLLAR("USD", "$"),
    EURO("EUR", "€");

    companion object {
        fun fromCode(currencyCode: String): Currency? {
            // Ищем элемент enum, у которого поле 'code' совпадает с переданным currencyCode (без учета регистра)
            return entries.find { it.code.equals(currencyCode, ignoreCase = true) }
        }
    }
}