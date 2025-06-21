package com.spendscan.features.expenses.myHistory.domain

import com.spendscan.features.expenses.myHistory.data.api.TransactionApiService
import com.spendscan.features.expenses.myHistory.data.models.TransactionDto
import android.util.Log
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException

class MyHistoryRepositoryImpl(private val apiService: TransactionApiService) : MyHistoryRepository {
    private val MAX_RETRIES = 3
    private val RETRY_DELAY_MS = 2000L // 2 секунды

    override suspend fun getTransactionsByPeriod(
        accountId: String,
        startDate: String?,
        endDate: String?
    ): List<TransactionDto> {
        var retryCount = 0
        while (retryCount < MAX_RETRIES) {
            try {
                return apiService.getTransactionsByPeriod(accountId, startDate, endDate)
            } catch (e: HttpException) {
                if (e.code() in 500..599) { // Если ошибка сервера 5xx
                    Log.e("MyHistoryRepository", "Server error (${e.code()}), retrying ${retryCount + 1}/${MAX_RETRIES}", e)
                    retryCount++
                    delay(RETRY_DELAY_MS)
                } else {
                    // Другие HTTP-ошибки (4xx) - не перезапрашиваем
                    Log.e("MyHistoryRepository", "HTTP error ${e.code()}: ${e.message()}", e)
                    throw e
                }
            } catch (e: IOException) {
                // Сетевые ошибки (нет интернета, таймаут и т.д.)
                Log.e("MyHistoryRepository", "Network error, retrying ${retryCount + 1}/${MAX_RETRIES}", e)
                retryCount++
                delay(RETRY_DELAY_MS)
            } catch (e: Exception) {
                // Неизвестные ошибки
                Log.e("MyHistoryRepository", "Unknown error while fetching transactions", e)
                throw e
            }
        }
        // Если все попытки исчерпаны
        throw IOException("Failed to fetch transactions after $MAX_RETRIES attempts.")
    }
}