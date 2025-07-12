package com.spendscan.spendscan.feature.categories.data.mapper

import com.spendscan.spendscan.core.domain.models.transaction.Category
import com.spendscan.spendscan.feature.categories.data.models.CategoryResponse

fun CategoryResponse.toDomain() = Category(
    id = this.id,
    name = this.name,
    emoji = this.emoji,
    isIncome = this.isIncome
)