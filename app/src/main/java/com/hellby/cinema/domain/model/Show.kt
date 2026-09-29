package com.hellby.cinema.domain.model

data class Show(
    val id: Int,
    val title: String,
    val shortDescription: String,
    val fullDescription: String,
    val imageUrl: String?,
    val posterUrl: String?,
    val genres: List<String>,
    val rating: Double?,
    val premiered: String?,
    val status: String?,
    val runtime: Int?,
    val language: String?,
    val network: String?,
    val weight: Int? = null
)

data class CastMember(
    val id: Int,
    val name: String,
    val character: String,
    val imageUrl: String?
)

data class ShowDetail(
    val show: Show,
    val cast: List<CastMember>,
    val officialSite: String?
)

data class HomeSection(
    val title: String,
    val shows: List<Show>
)

data class HomeContent(
    val featured: List<Show>,
    val sections: List<HomeSection>
)
