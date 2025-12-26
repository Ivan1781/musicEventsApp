package com.classic.event.dto

import constants.DefaultCities
import java.time.LocalDate
import java.time.LocalDateTime

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
)
