package com.hellby.cinema.data.remote

import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class TvMazeRemoteDataSource @Inject constructor(
    private val apiService: TvMazeApiService
) {
    suspend fun getShows(page: Int): Result<List<ShowDto>> = safeApiCall {
        apiService.getShows(page)
    }

    suspend fun getShow(id: Int): Result<ShowDto> = safeApiCall {
        apiService.getShow(id)
    }

    suspend fun getCast(id: Int): Result<List<CastDto>> = safeApiCall {
        apiService.getCast(id)
    }

    private suspend fun <T> safeApiCall(block: suspend () -> T): Result<T> {
        return try {
            Result.success(block())
        } catch (ce: CancellationException) {
            throw ce
        } catch (e: IOException) {
            Result.failure(e)
        } catch (e: SocketTimeoutException) {
            Result.failure(e)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: IllegalStateException) {
            Result.failure(e)
        } catch (e: RuntimeException) {
            Result.failure(e)
        }
    }
}
