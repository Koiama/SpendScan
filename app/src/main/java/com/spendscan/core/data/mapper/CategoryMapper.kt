package com.spendscan.core.data.mapper

import com.spendscan.core.data.models.CategoryDto
import com.spendscan.core.domain.models.Category

fun CategoryDto.toDomain(): Category {
    return Category(
        id = id,
        name = name,
        emoji = emoji,
        isIncome = isIncome
    )
}