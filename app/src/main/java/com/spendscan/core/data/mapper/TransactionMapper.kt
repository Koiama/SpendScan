package com.spendscan.core.data.mapper

import com.spendscan.core.data.models.TransactionDto
import com.spendscan.core.domain.models.Transaction
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

fun TransactionDto.toDomain(): Transaction {
    return Transaction(
        id = id,
        account = account.toDomain(),
        category = category.toDomain(),
        amount = this.amount.toDouble(),
        date = parseDateTime(transactionDate),
        comment = comment
    )
}

private fun parseDateTime(dateTimeString: String): LocalDateTime {
    return try {
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        ZonedDateTime.parse(dateTimeString, formatter)
            .withZoneSameInstant(ZoneId.systemDefault())
            .toLocalDateTime()
    } catch (e: DateTimeParseException) {
        LocalDateTime.now()
    }
}