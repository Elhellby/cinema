package com.hellby.cinema.data.remote

import com.hellby.cinema.data.remote.TvMazeDtos.CastDto
import com.hellby.cinema.data.remote.TvMazeDtos.ShowDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TvMazeApiService {
    @GET("shows")
    suspend fun getShows(@Query("page") page: Int): List<ShowDto>

    @GET("shows/{id}")
    suspend fun getShow(@Path("id") id: Int): ShowDto

    @GET("shows/{id}/cast")
    suspend fun getCast(@Path("id") id: Int): List<CastDto>
}
