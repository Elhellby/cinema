package com.hellby.cinema.data.repository

import com.hellby.cinema.data.mapper.toDetailDomain
import com.hellby.cinema.data.mapper.toDomain
import com.hellby.cinema.data.remote.TvMazeRemoteDataSource
import com.hellby.cinema.di.DefaultDispatcher
import com.hellby.cinema.domain.model.HomeContent
import com.hellby.cinema.domain.model.HomeSection
import com.hellby.cinema.domain.model.Show
import com.hellby.cinema.domain.model.ShowDetail
import com.hellby.cinema.domain.repository.CatalogRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CatalogRepositoryImpl @Inject constructor(
    private val remoteDataSource: TvMazeRemoteDataSource,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : CatalogRepository {
    private val homeCache = mutableMapOf<Long, HomeContent>()
    private val detailCache = mutableMapOf<Int, ShowDetail>()
    private val homeCacheTimestamp = mutableMapOf<Long, Long>()
    private val detailCacheTimestamp = mutableMapOf<Int, Long>()
    private val cacheTTL = TimeUnit.MINUTES.toMillis(5)

    override suspend fun getHomeContent(forceRefresh: Boolean): Result<HomeContent> = withContext(defaultDispatcher) {
        val cacheKey = 0L
        val now = System.currentTimeMillis()
        val cacheValid = !forceRefresh && homeCacheTimestamp[cacheKey]?.let { now - it < cacheTTL } == true
        if (cacheValid) {
            return@withContext Result.success(homeCache[cacheKey]!!)
        }

        val showsResult = remoteDataSource.getShows(0)
        val shows = showsResult.getOrElse { return@withContext Result.failure(it) }
        val mapped = shows.map { it.toDomain() }
        val featured = mapped.sortedByDescending { it.rating ?: 0.0 }.take(8)
        val sections = buildList {
            val latest = mapped.filter { !it.premiered.isNullOrBlank() }
                .sortedByDescending { it.premiered ?: "" }
            if (latest.isNotEmpty()) add(HomeSection("Nuevos", latest.take(8)))

            val popular = mapped.sortedByDescending { it.network?.length ?: 0 }
            if (popular.isNotEmpty()) add(HomeSection("Populares", popular.take(8)))

            val drama = mapped.filter { it.genres.any { genre -> genre.contains("Drama", ignoreCase = true) } }
            if (drama.isNotEmpty()) add(HomeSection("Drama", drama.take(8)))

            val comedy = mapped.filter { it.genres.any { genre -> genre.contains("Comedy", ignoreCase = true) } }
            if (comedy.isNotEmpty()) add(HomeSection("Comedia", comedy.take(8)))
        }

        val content = HomeContent(featured = featured, sections = sections.filter { it.shows.isNotEmpty() })
        homeCache[cacheKey] = content
        homeCacheTimestamp[cacheKey] = now
        Result.success(content)
    }

    override suspend fun getShowDetail(id: Int): Result<ShowDetail> = withContext(defaultDispatcher) {
        val now = System.currentTimeMillis()
        val cached = detailCache[id]
        if (cached != null && detailCacheTimestamp[id]?.let { now - it < cacheTTL } == true) {
            return@withContext Result.success(cached)
        }

        val detailResult = remoteDataSource.getShow(id)
        val detail = detailResult.getOrElse { return@withContext Result.failure(it) }
        val castResult = remoteDataSource.getCast(id)
        val cast = castResult.getOrElse { emptyList() }

        val domain = detail.toDetailDomain(cast, detail.officialSite)
        detailCache[id] = domain
        detailCacheTimestamp[id] = now
        Result.success(domain)
    }
}
