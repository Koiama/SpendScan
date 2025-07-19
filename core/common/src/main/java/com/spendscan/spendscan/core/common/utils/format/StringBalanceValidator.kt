package com.spendscan.spendscan.core.common.utils.format

fun String.toValidBalance(): String {
    val cleaned = this.replace(" ", "")

    val isNegative = cleaned.startsWith("-")
    val unsigned = if (isNegative) cleaned.drop(1) else cleaned

    val filtered = unsigned
        .filterIndexed { index, c ->
            c.isDigit() || (c == '.' && !unsigned.take(index).contains('.'))
        }
        .let { filteredInput ->
            val parts = filteredInput.split(".")
            val intPart = parts[0].take(13)
            val decPart = parts.getOrNull(1)?.take(2)
            if (decPart != null) "$intPart.$decPart" else intPart
        }
        .let { final ->
            if (isNegative) "-$final" else final
        }

    val isPartialNegative = this == "-" || this == "-."
    return if (isPartialNegative) this else filtered
}