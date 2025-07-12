package com.spendscan.spendscan.core.data.mapper.transaction

import com.spendscan.spendscan.core.data.models.transaction.CategoryResponse
import com.spendscan.spendscan.core.domain.models.transaction.Category

fun CategoryResponse.toDomain(): Category {
    return Category(
        id = id,
        name = name,
        emoji = emoji,
        isIncome = isIncome
    )
}
