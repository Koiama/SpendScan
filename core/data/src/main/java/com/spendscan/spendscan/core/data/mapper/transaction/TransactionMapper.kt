package com.spendscan.spendscan.core.data.mapper.transaction

import android.util.Log
import com.spendscan.spendscan.core.data.models.transaction.TransactionResponse
import com.spendscan.spendscan.core.domain.models.transaction.Transaction
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeParseException

fun TransactionResponse.toDomain(): Transaction {
    return Transaction(
        id = id,
        account = account.toDomain(),
        category = category.toDomain(),
        amount = amount,
        date = parseDateTime(transactionDate),
        comment = comment
    )
}


private fun parseDateTime(dateTimeString: String): LocalDateTime {
    return try {
        Log.d("TIME", "parseDateTime: $dateTimeString")
        val t = ZonedDateTime.parse(dateTimeString)
            .withZoneSameInstant(ZoneId.systemDefault())
            .toLocalDateTime()
        Log.d("TIME", "parseDateTime: $t")
        t
    } catch (e: DateTimeParseException) {
        LocalDateTime.now()
    }
}

