package com.spendscan.spendscan.core.common.utils

/**
 *  Обертка для результатов операций
 */
sealed class Result<out T> {
    class Success<out T>(val data: T): Result<T>()
    class Error(val code: Int, val message: String): Result<Nothing>()
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
