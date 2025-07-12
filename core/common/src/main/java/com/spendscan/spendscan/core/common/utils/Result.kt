package com.spendscan.spendscan.core.common.utils

/**
 * Обертка для представления результатов операций с четким разделением на случаи успешной обработки,
 * исключения и ошибки
 * @param T Тип возвращаемых данных при успешном выполнении
 */
sealed class Result<out T> {
    /**
     * Успешный результат операции, содержащий полученные данные
     *
     * @property data Результат выполнения операции
     */
    class Success<out T>(val data: T): Result<T>()
    /**
     * Ожидаемая ошибка в работе приложения с детализацией проблемы
     *
     * @property code Код ошибки для обработки (HTTP статус например)
     * @property message Описание ошибки
     */
    class Error(val code: Int, val message: String): Result<Nothing>()
    /**
     * Неперехваченное исключение или критическая ошибка
     *
     * @property error Исходный объект Throwable для логирования
     */
    class Exception(val error: Throwable): Result<Nothing>()
}

inline fun <T> Result<T>.onSuccess(action: (T) -> Unit): Result<T> {
    if (this is Result.Success) action(data)
    return this
}

inline fun <T> Result<T>.onError(action: (code: Int, message: String) -> Unit): Result<T> {
    if (this is Result.Error) action(code, message)
    return this
}

inline fun <T> Result<T>.onException(action: (Throwable) -> Unit): Result<T> {
    if (this is Result.Exception) action(error)
    return this
}

inline fun <T, R> Result<T>.map(transform: (T) -> R): Result<R> {
    return when (this) {
        is Result.Success -> Result.Success(transform(data))
        is Result.Error -> Result.Error(code, message)
        is Result.Exception -> Result.Exception(error)
    }
}
