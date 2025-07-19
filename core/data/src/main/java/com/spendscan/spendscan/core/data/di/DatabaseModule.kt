package com.spendscan.spendscan.core.data.di

import android.content.Context
import com.spendscan.spendscan.core.data.local.model.AppDatabase
import com.spendscan.spendscan.core.data.local.model.TransactionDao
import dagger.Module
import dagger.Provides
import dagger.Reusable
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): AppDatabase {
        return AppDatabase.getDatabase(context) // Используем наш синглтон-метод
    }

    @Provides
    @Reusable
    fun provideTransactionDao(appDatabase: AppDatabase): TransactionDao {
        return appDatabase.transactionDao()
    }
}