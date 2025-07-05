package com.spendscan.features.account.domain

import com.spendscan.core.common.Result
import com.spendscan.core.common.map
import com.spendscan.features.account.domain.models.Account
import com.spendscan.features.account.domain.models.Currency

class GetAccountUseCase(private val repository: AccountRepository) {
    suspend fun execute(accountId: Int? = null): Result<Account> {
        return repository.getAccounts().map { accounts ->
            if (accounts.isEmpty()) {
                throw NoSuchElementException("Нет доступных счетов")
            }
            accountId?.let { id -> accounts.find { it.id == id } }
                ?: accounts.first()
        }
    }
}

class UpdateAccountUseCase(private val repository: AccountRepository) {
    suspend fun execute(accountId: Int, account: Account): Result<Account> {
        return repository.updateAccount(accountId, account)
    }
}

class GetAvailableCurrenciesUseCase() {
    suspend fun execute(): Result<List<Currency>> {
        return Result.Success(Currency.entries.toList()) // <--- Используем Result.Success
    }
}