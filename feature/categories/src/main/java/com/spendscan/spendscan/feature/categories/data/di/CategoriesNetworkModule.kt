package com.spendscan.spendscan.feature.categories.data.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import com.spendscan.spendscan.feature.categories.data.api.CategoriesApi
import com.spendscan.spendscan.feature.categories.di.CategoriesScope

@Module
class CategoriesNetworkModule {

    @Provides
    @CategoriesScope
    fun provideCategoriesApi(retrofit: Retrofit): CategoriesApi = retrofit.create(CategoriesApi::class.java)
}