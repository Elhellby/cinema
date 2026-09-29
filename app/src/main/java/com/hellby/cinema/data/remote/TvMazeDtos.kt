package com.hellby.cinema.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShowDto(
    @SerialName("id") val id: Int? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("summary") val summary: String? = null,
    @SerialName("genres") val genres: List<String> = emptyList(),
    @SerialName("language") val language: String? = null,
    @SerialName("premiered") val premiered: String? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("runtime") val runtime: Int? = null,
    @SerialName("rating") val rating: RatingDto? = null,
    @SerialName("weight") val weight: Int? = null,
    @SerialName("network") val network: NetworkDto? = null,
    @SerialName("webChannel") val webChannel: NetworkDto? = null,
    @SerialName("image") val image: ImageDto? = null,
    @SerialName("officialSite") val officialSite: String? = null
)

@Serializable
data class RatingDto(
    @SerialName("average") val average: Double? = null
)

@Serializable
data class NetworkDto(
    @SerialName("name") val name: String? = null
)

@Serializable
data class ImageDto(
    @SerialName("medium") val medium: String? = null,
    @SerialName("original") val original: String? = null
)

@Serializable
data class CastDto(
    @SerialName("person") val person: PersonDto? = null,
    @SerialName("character") val character: CharacterDto? = null
)

@Serializable
data class PersonDto(
    @SerialName("id") val id: Int? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("image") val image: PersonImageDto? = null
)

@Serializable
data class PersonImageDto(
    @SerialName("medium") val medium: String? = null,
    @SerialName("original") val original: String? = null
)

@Serializable
data class CharacterDto(
    @SerialName("name") val name: String? = null
)
