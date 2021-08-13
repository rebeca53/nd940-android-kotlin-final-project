package com.rebeca.spacewallpaper.data.local

/**
 * A sealed class that encapsulates successful outcome with a value of type [T]
 * or a failure with message and statusCode
 */
sealed class RequestResult<out T : Any> {
    data class Success<out T : Any>(val data: T) : RequestResult<T>()
    data class Error(val message: String?, val statusCode: Int? = null) :
        RequestResult<Nothing>()
}