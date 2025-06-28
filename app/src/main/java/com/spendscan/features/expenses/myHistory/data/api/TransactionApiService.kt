package com.spendscan.features.expenses.myHistory.data.api

import com.spendscan.features.account.domain.models.Account
import com.spendscan.features.account.domain.models.AccountHistory
import com.spendscan.features.expenses.myHistory.data.models.TransactionDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import com.spendscan.features.expenses.myHistory.data.models.Category


interface TransactionApiService {

    @GET("transactions/account/{accountId}/period")
    suspend fun getTransactionsByPeriod(
        @Path("accountId") accountId: String,
        @Query("startDate") startDate: String?,
        @Query("endDate") endDate: String?
    ): List<TransactionDto>

    @GET("/accounts")
    suspend fun getAccounts(
    ): List<Account>

    @GET("/accounts/{id}")
    suspend fun getAccountById(
        @Path("Id") Id: Int,
    ): List<TransactionDto>

    @GET("/accounts/{id}/history")
    suspend fun getAccountHistoryById(
        @Path("Id") Id: Int,
    ): List<AccountHistory>

    @GET("/categories")
    suspend fun getCategories(
    ): List<Category>

    @GET("/categories/type/{isIncome}")
    suspend fun getCategoriesByType(
        @Path("isIncome") isIncome: Boolean
    ): List<Category>

}