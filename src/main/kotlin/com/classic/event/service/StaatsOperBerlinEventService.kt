package com.classic.event.service

import com.classic.event.config.StaatsOperBerlinProperties
import com.classic.event.dto.StaatsOperBerlinEventResponseDto
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Service

@Service
class StaatsOperBerlinEventService(
    private val remoteSiteService: RemoteSiteService,
    private val properties: StaatsOperBerlinProperties,
    private val parser: StaatsOperBerlinHtmlParser
) {
    private val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    fun fetchSchedule(date: LocalDate): StaatsOperBerlinEventResponseDto {
        val formattedDate = date.format(dateFormatter)
        val targetUrl = properties.url.format(formattedDate)
        val html =
            remoteSiteService.fetch(
                url = targetUrl,
                headers = properties.headers,
                queryParams = properties.params,
                responseType = object : ParameterizedTypeReference<String>() {}
            )
        return parser.parse(html)
    }
}
