package com.classic.event.service

import com.classic.event.dto.HannoverStaatsTheaterEventResponseDto
import constants.DatePattern.DATE_DASH_DMY
import java.time.LocalDate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Service
import properties.HannoverStaatsTheaterProperties

@Service
class HannoverStaatsTheaterEventService(
    private val remoteSiteService: RemoteSiteService,
    private val properties: HannoverStaatsTheaterProperties
) {
    fun fetchEvents(
        date: LocalDate = LocalDate.now(),
        pageNumber: String = "1"
    ): HannoverStaatsTheaterEventResponseDto {
        val responseBody =
            remoteSiteService.fetch(
                url = properties.url,
                headers = properties.headers,
                queryParams = properties.params + mapOf(
                    "date_from" to date.format(DATE_DASH_DMY),
                    "p" to pageNumber
                ),
                responseType = object : ParameterizedTypeReference<HannoverStaatsTheaterEventResponseDto>() {}
            )
        return responseBody
    }
}
