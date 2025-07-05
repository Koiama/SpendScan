package com.spendscan.core.data.api

import com.spendscan.core.data.models.AccountBriefDto
import com.spendscan.core.data.models.AccountFullDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * API для загрузки счетов
 */
interface AccountApiService {
    @GET("accounts")
    suspend fun getAccounts(): Response<List<AccountFullDto>>

    @PUT("accounts/{accountId}")
    suspend fun updateAccount(
        @Path("accountId") accountId: Int,
        @Body account: AccountBriefDto
    ): Response<AccountFullDto>
}
