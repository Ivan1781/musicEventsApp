package com.classic.event.service

import com.classic.event.dto.DresdenSemperOperEventDto
import com.classic.event.dto.DresdenSemperOperEventListDto
import com.classic.event.entity.EventEntity
import com.classic.event.parsers.SemperOperDresdenParser
import java.time.LocalDateTime
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Service
import properties.SemperOperDresdenProperties

@Service
class SemperOperDresdenEventService(
    private val remoteSiteService: RemoteSiteService,
    private val properties: SemperOperDresdenProperties
) {
    fun fetchEvents(eventNumber: Int): DresdenSemperOperEventListDto {
        val responseBody =
            remoteSiteService.fetch(
                url = properties.url,
                headers = properties.headers,
                queryParams = properties.params + mapOf(
                    "loadingCount" to eventNumber.toString()),
                responseType = object : ParameterizedTypeReference<String>() {}
            )

        return SemperOperDresdenParser.parse(responseBody)
    }

    fun toEvents(response: DresdenSemperOperEventListDto): List<EventEntity> =
        response.events
            .mapNotNull { dto -> toEntity(dto) }
            .distinctBy { it.detailUrl }

    private fun toEntity(dto: DresdenSemperOperEventDto): EventEntity? {
        val normalizedTitle = dto.title.trim().ifBlank { return null }
        val normalizedDetailUrl = dto.detailUrl.trim().ifBlank { null }
        val normalizedCity = dto.city.trim().ifBlank { null }
        val normalizedCategory = dto.category.trim().ifBlank { null }
        val normalizedAuthor = dto.author.trim().ifBlank { null }
        val normalizedStatus = dto.status.trim().ifBlank { null }
        val parsedDateTime = dto.dateTime
            .trim()
            .takeIf { it.isNotEmpty() }
            ?.let { LocalDateTime.parse(it) }

        return EventEntity(
            title = normalizedTitle,
            city = normalizedCity,
            detailUrl = normalizedDetailUrl,
            dateTime = parsedDateTime,
            price = normalizedStatus,
            category = normalizedCategory,
            author = normalizedAuthor
        )
    }
}
