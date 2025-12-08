package com.classic.event.dto

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
    val id: Long,
    val dateTime: LocalDateTime,
    val weekdayLabel: String?,
    val dateLabel: String?,
    val timeLabel: String?,
    val venueName: String?,
    val venueUrl: String?,
    val title: String,
    val detailUrl: String,
    val workInfo: String?,
    val tags: List<String> = emptyList(),
    val bookingNote: String?,
    val duration: String?,
    val ticketUrl: String?,
    val priceText: String?,
    val performers: List<StaatsOperBerlinPerformerDto> = emptyList(),
    val program: List<StaatsOperBerlinProgramItemDto> = emptyList()
) {
    override fun toString(): String {
        return "Event: $title - $dateTime"
    }
}

data class StaatsOperBerlinPerformerDto(
    val role: String?,
    val name: String,
    val profileUrl: String?
)

data class StaatsOperBerlinProgramItemDto(
    val artist: String?,
    val piece: String?
)
