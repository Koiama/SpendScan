package com.spendscan.spendscan.feature.categories.di

import com.spendscan.spendscan.feature.categories.domain.repository.CategoriesRepository

interface CategoriesComponentDeps {
    fun categoriesRepository(): CategoriesRepository
}
