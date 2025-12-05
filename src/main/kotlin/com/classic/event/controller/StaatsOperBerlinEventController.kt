package com.classic.event.controller

import com.classic.event.dto.StaatsOperBerlinEventResponseDto
import com.classic.event.service.StaatsOperBerlinEventService
import java.time.LocalDate
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/events")
class StaatsOperBerlinEventController(
    private val eventService: StaatsOperBerlinEventService,
) {
    @GetMapping("/staatsoper")
    fun fetchEvents(
        @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) date: LocalDate,
    ): ResponseEntity<StaatsOperBerlinEventResponseDto> =
        ResponseEntity.ok(eventService.fetchSchedule(date))
}
