package com.spendscan.features.incomes.myHistory.data.models

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun String.toFormattedTime(): String {
    return try {
        // Парсим строку в Instant (момент времени в UTC)
        val instant = Instant.parse(this)
        val localTime = instant.atZone(ZoneId.systemDefault()).toLocalTime()
        val formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
        localTime.format(formatter)
    } catch (e: Exception) {
        e.printStackTrace()
        "Неизвестно"
    }
}