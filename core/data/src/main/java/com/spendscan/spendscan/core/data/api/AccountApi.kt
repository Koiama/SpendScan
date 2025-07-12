package com.spendscan.spendscan.core.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import com.spendscan.spendscan.core.data.models.account.AccountExtendedInfoResponse
import com.spendscan.spendscan.core.domain.models.account.AccountUpdateInfo

interface AccountApi {
    @GET("accounts")
    suspend fun getAllUsersAccounts(): Response<List<AccountExtendedInfoResponse>>

    @PUT("accounts/{id}")
    suspend fun updateAccountById(
        @Path("id") id: Long,
        @Body updateInfo: AccountUpdateInfo
    ): Response<AccountExtendedInfoResponse>
}