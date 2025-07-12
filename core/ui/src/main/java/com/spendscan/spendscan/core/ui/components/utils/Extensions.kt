package com.spendscan.spendscan.core.ui.components.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter


fun Long.toLocalDate(): LocalDate {
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}

fun LocalDate.formatForDisplay(): String {
    return format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
}
