package com.classic.event.dto

import constants.DefaultCities
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class StaatsOperBerlinEventResponseDto(
    val monthTitle: String,
    val days: List<StaatsOperBerlinDayDto>,
    val nextPageUrl: String?
)

data class StaatsOperBerlinDayDto(
    val date: LocalDate,
    val events: List<StaatsOperBerlinEventDto>
)

data class StaatsOperBerlinEventDto(
    val dateTime: LocalDateTime,
    val venueName: String?,
    val title: String,
    val detailUrl: String,
    val workInfo: String?,
    val duration: String?,
    val ticketUrl: String?,
    val priceText: String?,
    val category: String?,
    val city: String = DefaultCities.BERLIN
) {
    fun toEvent(): EventDto =
        EventDto(
            title = title,
            city = city,
            detailUrl = detailUrl,
            dateTime = dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            duration = duration,
            location = venueName,
            price = priceText,
            ticketUrl = ticketUrl,
            category = category,
            author = workInfo
        )
}

fun StaatsOperBerlinEventResponseDto.toEvents(): List<EventDto> =
    days.flatMap { day -> day.events.map { it.toEvent() } }
