package com.spendscan.spendscan.feature.categories.data.di

import dagger.Binds
import dagger.Module
import com.spendscan.spendscan.feature.categories.data.repository.CategoriesRepositoryImpl
import com.spendscan.spendscan.feature.categories.di.CategoriesScope
import com.spendscan.spendscan.feature.categories.domain.repository.CategoriesRepository


/**
 * Модуль для DI связанного с репозиторием категорий
 */
@Module
interface CategoriesRepositoryModule {
    @Binds
    @CategoriesScope
    fun bindCategoriesRepositoryImpl(repositoryImpl: CategoriesRepositoryImpl): CategoriesRepository
}
