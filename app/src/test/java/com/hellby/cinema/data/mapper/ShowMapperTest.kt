package com.hellby.cinema.data.mapper

import com.hellby.cinema.data.remote.CastDto
import com.hellby.cinema.data.remote.CharacterDto
import com.hellby.cinema.data.remote.ImageDto
import com.hellby.cinema.data.remote.NetworkDto
import com.hellby.cinema.data.remote.PersonDto
import com.hellby.cinema.data.remote.PersonImageDto
import com.hellby.cinema.data.remote.ShowDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ShowMapperTest {

    @Test
    fun `toDomain limpia las etiquetas html del summary`() {
        val dto = ShowDto(
            id = 1,
            name = "Show",
            summary = "<p>Una <b>historia</b> increíble.</p>"
        )

        val result = dto.toDomain()

        assertTrue(!result.fullDescription.contains("<"))
        assertEquals("Una historia increíble.", result.fullDescription)
    }

    @Test
    fun `toDomain trunca la descripcion corta sin cortar palabras a 120 caracteres`() {
        val longText = "Palabra ".repeat(30).trim()
        val dto = ShowDto(id = 1, name = "Show", summary = longText)

        val result = dto.toDomain()

        assertTrue(result.shortDescription.length <= 123) // 120 + "..."
        assertTrue(!result.shortDescription.dropLast(3).endsWith(" "))
        assertTrue(result.shortDescription.endsWith("..."))
    }

    @Test
    fun `toDomain no trunca descripciones cortas`() {
        val dto = ShowDto(id = 1, name = "Show", summary = "Corto")

        val result = dto.toDomain()

        assertEquals("Corto", result.shortDescription)
    }

    @Test
    fun `toDomain maneja imagen nula sin crashear`() {
        val dto = ShowDto(id = 1, name = "Show", image = null)

        val result = dto.toDomain()

        assertNull(result.imageUrl)
        assertNull(result.posterUrl)
    }

    @Test
    fun `toDomain fuerza https en urls de imagen que empiezan con doble slash`() {
        val dto = ShowDto(
            id = 1,
            name = "Show",
            image = ImageDto(medium = "//static.tvmaze.com/img.jpg", original = "//static.tvmaze.com/orig.jpg")
        )

        val result = dto.toDomain()

        assertEquals("https://static.tvmaze.com/orig.jpg", result.imageUrl)
    }

    @Test
    fun `toDomain conserva urls que ya son https`() {
        val dto = ShowDto(
            id = 1,
            name = "Show",
            image = ImageDto(original = "https://static.tvmaze.com/orig.jpg")
        )

        val result = dto.toDomain()

        assertEquals("https://static.tvmaze.com/orig.jpg", result.imageUrl)
    }

    @Test
    fun `toDomain usa network o webChannel segun disponibilidad`() {
        val dto = ShowDto(id = 1, name = "Show", network = NetworkDto(name = "HBO"))

        val result = dto.toDomain()

        assertEquals("HBO", result.network)
    }

    @Test
    fun `CastDto toDomain mapea nombre y personaje`() {
        val castDto = CastDto(
            person = PersonDto(id = 5, name = "Actor", image = PersonImageDto(medium = "//img.jpg")),
            character = CharacterDto(name = "Personaje")
        )

        val result = castDto.toDomain()

        assertEquals(5, result.id)
        assertEquals("Actor", result.name)
        assertEquals("Personaje", result.character)
        assertEquals("https://img.jpg", result.imageUrl)
    }

    private fun RatingNetwork(name: String) = com.hellby.cinema.data.remote.NetworkDto(name = name)
}
