package com.hellby.cinema.domain.usecase

import com.hellby.cinema.domain.model.ShowDetail
import com.hellby.cinema.domain.repository.CatalogRepository
import javax.inject.Inject

class GetShowDetailUseCase @Inject constructor(
    private val catalogRepository: CatalogRepository
) {
    suspend operator fun invoke(id: Int): Result<ShowDetail> = catalogRepository.getShowDetail(id)
}
