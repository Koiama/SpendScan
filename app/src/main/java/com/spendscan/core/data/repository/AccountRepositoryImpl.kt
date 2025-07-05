package com.spendscan.core.data.repository

import com.spendscan.core.data.api.AccountApiService
import com.spendscan.core.data.mapper.toBriefDto
import com.spendscan.core.data.mapper.toDomain
import com.spendscan.features.account.domain.AccountRepository
import com.spendscan.features.account.domain.models.Account
import com.spendscan.core.common.Result


class AccountRepositoryImpl(
    private val accountApiService: AccountApiService
) : AccountRepository {

    override suspend fun getAccounts(): Result<List<Account>> {
        return try {
            val response = accountApiService.getAccounts() // Возвращает Response<List<AccountFullDto>>
            if (response.isSuccessful) {
                val accountFullDtos = response.body() ?: emptyList()
                val accounts = accountFullDtos.map { it.toDomain() }
                Result.Success(accounts) // <--- Используем Result.Success
            } else {
                // Если API вернул ошибку, но это не исключение (например, 404, 401, 500)
                Result.Error(response.code(), response.message()) // <--- Используем Result.Error
            }
        } catch (e: Exception) {
            // Если произошла сетевая ошибка, ошибка парсинга или другая JVM-исключение
            Result.Exception(e) // <--- Используем Result.Exception
        }
    }

    override suspend fun updateAccount(accountId: Int, account: Account): Result<Account> {
        return try {
            val requestDto = account.toBriefDto()
            val response = accountApiService.updateAccount(accountId, requestDto) // Возвращает Response<AccountFullDto>
            if (response.isSuccessful) {
                val updatedAccountFullDto = response.body()
                if (updatedAccountFullDto != null) {
                    Result.Success(updatedAccountFullDto.toDomain()) // <--- Используем Result.Success
                } else {
                    Result.Error(response.code(), "Ответ API пустой после обновления") // <--- Ошибка, если тело пусто
                }
            } else {
                Result.Error(response.code(), response.message()) // <--- Используем Result.Error
            }
        } catch (e: Exception) {
            Result.Exception(e) // <--- Используем Result.Exception
        }
    }
}