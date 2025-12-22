package com.classic.event.service

import com.classic.event.entity.EventEntity
import com.classic.event.dto.StaatsOperBerlinEventResponseDto
import com.classic.event.dto.StaatsOperBerlinEventDto
import com.classic.event.parsers.StaatsOperBerlinHtmlParser
import constants.DatePattern.DATE_DASH_DMY
import properties.StaatsOperBerlinProperties
import java.time.LocalDate
import java.util.Locale
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Service

@Service
class StaatsOperBerlinEventService(
    private val remoteSiteService: RemoteSiteService,
    private val properties: StaatsOperBerlinProperties
) {
    private val siteBaseUrl =
        properties.url.substringBefore("/de/spielplan").ifBlank { "https://www.staatsoper-berlin.de" }

    fun fetchSchedule(date: LocalDate): StaatsOperBerlinEventResponseDto {
        val formattedDate = date.format(DATE_DASH_DMY)
        val targetUrl = properties.url.format(formattedDate)
        val html =
            remoteSiteService.fetch(
                url = targetUrl,
                headers = properties.headers,
                queryParams = properties.params,
                responseType = object : ParameterizedTypeReference<String>() {}
            )
        return StaatsOperBerlinHtmlParser.parse(html)
    }

    fun mapToEvents(response: StaatsOperBerlinEventResponseDto): List<EventEntity> =
        response.days
            .flatMap { it.events }
            .mapNotNull { dto -> toEntity(dto) }
            .distinctBy { it.detailUrl ?: "${it.title}|${it.dateTime}" }

    private fun toEntity(dto: StaatsOperBerlinEventDto): EventEntity? {
        val normalizedTitle = dto.title.trim().ifBlank { return null }
        val normalizedDetailUrl = normalizeUrl(dto.detailUrl)
        val normalizedTicketUrl = normalizeUrl(dto.ticketUrl)

        return EventEntity(
            title = normalizedTitle,
            city = "Berlin",
            detailUrl = normalizedDetailUrl,
            dateTime = dto.dateTime,
            duration = dto.duration?.clean(),
            location = dto.venueName?.clean()?.replaceFirstChar { it.titlecase(Locale.getDefault()) },
            price = dto.priceText?.clean(),
            ticketUrl = normalizedTicketUrl,
            category = dto.category,
            author = dto.workInfo
        )
    }

    private fun normalizeUrl(raw: String?): String? {
        val cleaned = raw?.trim()?.takeIf { it.isNotEmpty() } ?: return null
        return if (cleaned.startsWith("http", ignoreCase = true)) {
            cleaned
        } else {
            "${siteBaseUrl.trimEnd('/')}/${cleaned.trimStart('/')}"
        }
    }

    private fun String.clean(): String? =
        replace(Regex("\\s+"), " ")
            .trim()
            .takeIf { it.isNotEmpty() }
}
