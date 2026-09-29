package com.hellby.cinema.domain.usecase

import com.hellby.cinema.domain.model.HomeContent
import com.hellby.cinema.domain.repository.CatalogRepository
import javax.inject.Inject

class GetHomeContentUseCase @Inject constructor(
    private val catalogRepository: CatalogRepository
) {
    suspend operator fun invoke(forceRefresh: Boolean = false): Result<HomeContent> =
        catalogRepository.getHomeContent(forceRefresh)
}
