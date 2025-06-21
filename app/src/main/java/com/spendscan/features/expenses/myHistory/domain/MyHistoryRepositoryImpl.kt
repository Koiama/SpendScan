package com.spendscan.features.expenses.myHistory.domain

import com.spendscan.features.expenses.myHistory.data.api.TransactionApiService
import com.spendscan.features.expenses.myHistory.data.models.TransactionDto
import android.util.Log
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException

class MyHistoryRepositoryImpl(private val apiService: TransactionApiService) : MyHistoryRepository {
    private val MAX_RETRIES = 3
    private val RETRY_DELAY_MS = 2000L

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
                // Обработка HTTP-ошибок (например, 5xx)
                if (e.code() in 500..599) { // Если ошибка сервера
                    Log.e("MyHistoryRepository", "Ошибка сервера (${e.code()}), попытка перезапроса ${retryCount + 1}", e)
                    retryCount++
                    delay(RETRY_DELAY_MS)
                } else {
                    // Другие HTTP-ошибки (4xx) - не перезапрашиваем, просто пробрасываем
                    Log.e("MyHistoryRepository", "HTTP ошибка ${e.code()}: ${e.message()}", e)
                    throw e
                }
            } catch (e: IOException) {
                // Обработка сетевых ошибок (нет интернета, таймаут и т.д.)
                Log.e("MyHistoryRepository", "Сетевая ошибка, попытка перезапроса ${retryCount + 1}", e)
                retryCount++
                delay(RETRY_DELAY_MS)
            } catch (e: Exception) {
                // Неизвестные ошибки - не перезапрашиваем, пробрасываем
                Log.e("MyHistoryRepository", "Неизвестная ошибка при получении транзакций", e)
                throw e
            }
        }
        throw IOException("Не удалось получить транзакции после $MAX_RETRIES попыток.")
    }
}