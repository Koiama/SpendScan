package com.spendscan.features.expenses.myHistory.data.models

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun String.toFormattedTime(): String {
    return try {
        // Парсим строку в Instant (момент времени в UTC)
        val instant = Instant.parse(this)
        val zonedDateTime = instant.atZone(ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("dd.MM, HH:mm", Locale.getDefault())
        zonedDateTime.format(formatter)
    } catch (e: Exception) {
        e.printStackTrace()
        "Неизвестно"
    }
}