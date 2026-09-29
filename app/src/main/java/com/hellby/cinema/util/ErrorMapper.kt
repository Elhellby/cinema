package com.hellby.cinema.util

import com.hellby.cinema.R
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

/** Traduce excepciones de red/serialización a un recurso de string amigable para el usuario. */
fun Throwable.toUserMessageRes(): Int = when (this) {
    is SocketTimeoutException -> R.string.error_timeout
    is HttpException -> R.string.error_server
    is SerializationException -> R.string.error_server
    is IOException -> R.string.error_no_connection
    else -> R.string.error_generic
}
