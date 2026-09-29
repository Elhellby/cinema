package com.hellby.cinema.data.mapper

import com.hellby.cinema.data.remote.CastDto
import com.hellby.cinema.data.remote.ShowDto
import com.hellby.cinema.domain.model.CastMember
import com.hellby.cinema.domain.model.Show
import com.hellby.cinema.domain.model.ShowDetail

internal fun ShowDto.toDomain(): Show {
    val description = summary.orEmpty().stripHtml().trim()
    return Show(
        id = id ?: 0,
        title = name.orEmpty(),
        shortDescription = description.truncateDescription(),
        fullDescription = description,
        imageUrl = image?.original?.normalizeUrl() ?: image?.medium?.normalizeUrl(),
        posterUrl = image?.original?.normalizeUrl() ?: image?.medium?.normalizeUrl(),
        genres = genres,
        rating = rating?.average,
        premiered = premiered,
        status = status,
        runtime = runtime,
        language = language,
        network = network?.name ?: webChannel?.name
    )
}

internal fun CastDto.toDomain(): CastMember {
    return CastMember(
        id = person?.id ?: 0,
        name = person?.name.orEmpty(),
        character = character?.name.orEmpty(),
        imageUrl = person?.image?.medium?.normalizeUrl() ?: person?.image?.original?.normalizeUrl()
    )
}

internal fun ShowDto.toDetailDomain(cast: List<CastDto>, officialSiteOverride: String? = null): ShowDetail {
    return ShowDetail(
        show = this.toDomain().copy(
            fullDescription = this.summary.orEmpty().stripHtml().trim(),
            shortDescription = this.summary.orEmpty().stripHtml().trim().truncateDescription()
        ),
        cast = cast.map { it.toDomain() },
        officialSite = officialSiteOverride ?: officialSite
    )
}

private fun String.stripHtml(): String {
    return replace(Regex("<[^>]*>"), " ")
        .replace("\\s+".toRegex(), " ")
        .trim()
}

private fun String.truncateDescription(maxLength: Int = 120): String {
    if (length <= maxLength) return this
    val cut = substring(0, maxLength)
    val lastSpace = cut.lastIndexOf(' ')
    val safeCut = if (lastSpace > 0) lastSpace else maxLength
    return substring(0, safeCut).trimEnd() + "..."
}

private fun String.normalizeUrl(): String {
    return if (startsWith("//")) "https:$this" else this
}
