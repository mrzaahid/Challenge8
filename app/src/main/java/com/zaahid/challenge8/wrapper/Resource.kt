package com.zaahid.challenge8.wrapper

sealed class Resource<T>(
    val payload: T? = null,
    val message: String? = null,
    val exception: Exception? = null,
    val throwable: Throwable? = null
) {
    class Success<T>(data: T  ) : Resource<T>(data)
    class Empty<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(exception: Exception?, data: T? = null) : Resource<T>(data, exception = exception)
    class ErrorTrow<T>(throwable : Throwable, data: T? = null) : Resource<T>(data,throwable = throwable )
    class Loading<T>(data: T? = null) : Resource<T>(data)
}