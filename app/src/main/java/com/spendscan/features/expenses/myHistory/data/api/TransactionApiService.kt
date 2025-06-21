package com.spendscan.features.expenses.myHistory.data.api

import com.spendscan.features.expenses.myHistory.data.models.TransactionDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TransactionApiService {

    @GET("transactions/account/{accountId}/period")
    suspend fun getTransactionsByPeriod(
        @Path("accountId") accountId: String,
        @Query("startDate") startDate: String?,
        @Query("endDate") endDate: String?
    ): List<TransactionDto>
}