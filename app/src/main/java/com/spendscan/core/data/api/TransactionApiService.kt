package com.spendscan.core.data.api

import com.spendscan.features.account.domain.models.Account
import com.spendscan.features.account.domain.models.AccountHistory
import com.spendscan.core.data.models.CategoryDto
import com.spendscan.core.data.models.TransactionDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * API для работы с банковскими транзакциями
 */

interface TransactionApiService {

    @GET("transactions/account/{accountId}/period")
    suspend fun getTransactionsByPeriod(
        @Path("accountId") accountId: Int,
        @Query("startDate") startDate: String?,
        @Query("endDate") endDate: String?
    ): Response<List<TransactionDto>>

}