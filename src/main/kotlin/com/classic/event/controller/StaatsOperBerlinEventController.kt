package com.classic.event.controller

import com.classic.event.dto.StaatsOperBerlinEventResponseDto
import com.classic.event.service.StaatsOperBerlinEventService
import java.time.LocalDate
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class StaatsOperBerlinEventController(
    private val eventService: StaatsOperBerlinEventService
) {
    fun fetchEvents(
        @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) date: LocalDate
    ): ResponseEntity<StaatsOperBerlinEventResponseDto> {
        return ResponseEntity.ok(eventService.fetchSchedule(date))
    }
}
