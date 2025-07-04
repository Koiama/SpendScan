package com.spendscan.features.articles.domain

// UseCase для получения статей
// Этот класс будет отвечать за бизнес-логику получения данных,
// заглушка.
class GetArticlesUseCase {
    suspend fun execute(): Result<ArticleRepo> {
        return try {
            // Имитация сетевого запроса
            kotlinx.coroutines.delay(1000)
            val dummyArticles = listOf(
                Article(1, "Продукты", "🥦"),
                Article(2, "Транспорт", "🚌"),
                Article(3, "Развлечения", "🍿"),
                Article(4, "Коммунальные", "💡"),
                Article(5, "Одежда", "👕"),
                Article(6, "Здоровье", "💊")
            )
            Result.success(ArticleRepo(categories = dummyArticles))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}