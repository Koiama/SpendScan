package com.spendscan.spendscan.core.common.utils.format

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun formatTimeWithLeadingZero(dateTime: LocalDateTime): String {
    val hour = dateTime.hour
    val minute = dateTime.minute

    val hourFormatted = if (hour < 10) "0$hour" else "$hour"
    val minuteFormatted = if (minute < 10) "0$minute" else "$minute"

    return "$hourFormatted:$minuteFormatted"
}

fun LocalDateTime.toUtcIsoString(
    zoneId: ZoneId = ZoneId.of("Europe/Moscow")
): String {
    return this
        .atZone(zoneId)
        .withZoneSameInstant(ZoneOffset.UTC)
        .format(DateTimeFormatter.ISO_INSTANT)
}
