package com.hellby.cinema.domain.repository

import com.hellby.cinema.domain.model.HomeContent
import com.hellby.cinema.domain.model.ShowDetail

interface CatalogRepository {
    suspend fun getHomeContent(forceRefresh: Boolean = false): Result<HomeContent>
    suspend fun getShowDetail(id: Int): Result<ShowDetail>
}
