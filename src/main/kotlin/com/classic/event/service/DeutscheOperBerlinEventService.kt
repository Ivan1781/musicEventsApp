package com.classic.event.service

import com.classic.event.dto.DeutscheOperBerlinEventResponseDto
import com.classic.event.dto.EventDto
import com.classic.event.entity.EventEntity
import com.fasterxml.jackson.databind.ObjectMapper
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import properties.DeutscheOperBerlinProperties

@Service
class DeutscheOperBerlinEventService(
    private val remoteSiteService: RemoteSiteService,
    private val properties: DeutscheOperBerlinProperties
) {
    private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    fun fetchEvents(
        date: LocalDate = LocalDate.now(),
        pageNumber: String = "1"
    ): DeutscheOperBerlinEventResponseDto {
        val responseBody =
            remoteSiteService.fetch(
                url = properties.url,
                headers = properties.headers,
                queryParams = properties.params + mapOf(
                    "date" to date.format(dateFormatter),
                    "p" to pageNumber
                ),
                responseType = object : ParameterizedTypeReference<DeutscheOperBerlinEventResponseDto>() {}
            )

        return responseBody
    }

    fun mapToDtos(response: DeutscheOperBerlinEventResponseDto): List<EventDto> =
        response.toEvents()
}
