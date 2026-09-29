package com.hellby.cinema.util

import com.hellby.cinema.R
import kotlinx.serialization.SerializationException
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

class ErrorMapperTest {

    @Test
    fun `SocketTimeoutException se traduce a error_timeout`() {
        assertEquals(R.string.error_timeout, SocketTimeoutException().toUserMessageRes())
    }

    @Test
    fun `HttpException se traduce a error_server`() {
        val httpException = HttpException(Response.error<Any>(500, "".toResponseBody(null)))
        assertEquals(R.string.error_server, httpException.toUserMessageRes())
    }

    @Test
    fun `SerializationException se traduce a error_server`() {
        assertEquals(R.string.error_server, SerializationException().toUserMessageRes())
    }

    @Test
    fun `IOException generica se traduce a error_no_connection`() {
        assertEquals(R.string.error_no_connection, IOException().toUserMessageRes())
    }

    @Test
    fun `excepcion desconocida se traduce a error_generic`() {
        assertEquals(R.string.error_generic, RuntimeException().toUserMessageRes())
    }
}
