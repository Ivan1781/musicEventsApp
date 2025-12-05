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
    val tags: List<String> = emptyList(),        // e.g., "Zum letzten Mal in dieser Spielzeit"
    val bookingNote: String?,                   // e.g., "Nur für Gäste der Abendvorstellung buchbar"
    val duration: String?,                      // e.g., "Dauer: ca. 2:15 h inklusive einer Pause"
    val ticketUrl: String?,
    val priceText: String?,                     // compact price string, as shown
    val performers: List<StaatsOperBerlinPerformerDto> = emptyList(),
    val program: List<StaatsOperBerlinProgramItemDto> = emptyList(),
)

data class StaatsOperBerlinPerformerDto(
    val role: String?,
    val name: String,
    val profileUrl: String?
)

data class StaatsOperBerlinProgramItemDto(
    val artist: String?,
    val piece: String?
)
