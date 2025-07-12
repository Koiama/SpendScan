package com.spendscan.spendscan.core.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import com.spendscan.spendscan.core.data.models.transaction.CreateTransactionRequest
import com.spendscan.spendscan.core.data.models.transaction.TransactionResponse
import com.spendscan.spendscan.core.data.models.transaction.UpdateTransactionRequest

/**
 * API для работы с банковскими транзакциями
 */
interface TransactionApi {
    /**
     * @param accountId Идентификатор банковского счета (обязательный)
     * @param startDate Начальная дата периода в формате в формате ISO 8601 (обязательный)
     * @param endDate Конечная дата периода в формате в формате ISO 8601 (обязательный)
     *
     * @return [TransactionResponse] обернутый в [Response] класс
     */
    @GET("transactions/account/{accountId}/period")
    suspend fun getTransactionsByAccountAndPeriod(
        @Path("accountId") accountId: Long,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Response<List<TransactionResponse>>

    @POST("transactions")
    suspend fun createTransaction(
        @Body request: CreateTransactionRequest
    ): Response<TransactionResponse>

    @PUT("transactions/{id}")
    suspend fun updateTransaction(
        @Path("id") id: Long,
        @Body updateTransactionRequest: UpdateTransactionRequest
    ): Response<TransactionResponse>

    @GET("transactions/{id}")
    suspend fun getTransactionById(
        @Path("id") id: Long
    ): Response<TransactionResponse>

    @DELETE("transactions/{id}")
    suspend fun deleteTransaction(
        @Path("id") id: Long
    ): Response<Unit>
}
