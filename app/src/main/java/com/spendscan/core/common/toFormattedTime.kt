package com.spendscan.core.common

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Функция для форматирования даты
 */
fun toFormattedTime(dateTime: LocalDateTime): String {
    val hour = dateTime.hour
    val minute = dateTime.minute

    val hourFormatted = if (hour < 10) "0$hour" else "$hour"
    val minuteFormatted = if (minute < 10) "0$minute" else "$minute"

    return "$hourFormatted:$minuteFormatted"
}