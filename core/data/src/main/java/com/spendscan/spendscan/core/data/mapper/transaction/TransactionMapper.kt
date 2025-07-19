package com.spendscan.spendscan.core.data.mapper.transaction

import android.util.Log
import com.spendscan.spendscan.core.data.local.model.TransactionEntity
import com.spendscan.spendscan.core.data.models.transaction.TransactionResponse
import com.spendscan.spendscan.core.domain.models.account.AccountBrief
import com.spendscan.spendscan.core.domain.models.account.Currency
import com.spendscan.spendscan.core.domain.models.transaction.Category
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

fun TransactionResponse.toEntity(): TransactionEntity {
    return TransactionEntity(
        id = id,
        accountId = account.id,
        categoryId = category.id,
        amount = amount,
        transactionDate = parseDateTime(transactionDate),
        comment = comment,
        createdAt = parseDateTime(createdAt),
        updatedAt = updatedAt?.let { parseDateTime(it) },
        currencyCode = account.currency
    )
}

fun Transaction.toEntity(): TransactionEntity {
    return TransactionEntity(
        id = id,
        accountId = account.id,
        categoryId = category.id,
        amount = amount,
        transactionDate = date,
        comment = comment,
        createdAt = LocalDateTime.now(),
        updatedAt = null,
        currencyCode = account.currency.name
    )
}

fun TransactionEntity.toDomain(): Transaction {
    val mappedCurrency = try {
        Currency.valueOf(currencyCode)
    } catch (e: IllegalArgumentException) {
        Log.e("TransactionMapper", "Unknown currency code: $currencyCode", e)
        Currency.RUB
    }

    val accountBrief = AccountBrief(
        id = accountId,
        name = "Счет: $accountId",
        currency = mappedCurrency,
        balance = 0.0
    )

    val category = Category(
        id = categoryId,
        name = "Категория: $categoryId",
        emoji = "\uD83D\uDCDD",
        isIncome = false
    )

    return Transaction(
        id = id,
        account = accountBrief,
        category = category,
        amount = amount,
        date = transactionDate,
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

