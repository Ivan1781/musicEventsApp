package com.classic.event.controller

import com.classic.event.dto.StaatsOperBerlinEventResponseDto
import com.classic.event.service.EventPersistenceService
import com.classic.event.service.StaatsOperBerlinEventService
import java.time.LocalDate
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.format.DateTimeFormatter

@RestController
class StaatsOperBerlinEventController(
    private val eventService: StaatsOperBerlinEventService,
    private val eventPersistenceService: EventPersistenceService
) {

    @GetMapping("/save")
    fun persistData() {
        var date = LocalDate.now().withDayOfMonth(1)

        for (a in 0..1) {
            val response = this.fetchEvents(date)
            val body = response.body!!
            val entities = eventService.mapToEvents(body)

            if (entities.isNotEmpty()) {
                eventPersistenceService.saveAll(entities)
            }

            date = LocalDate.parse(
                body.nextPageUrl!!.split("/").last(), DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        }
    }

    fun fetchEvents(date: LocalDate): ResponseEntity<StaatsOperBerlinEventResponseDto> {
        return ResponseEntity.ok(eventService.fetchSchedule(date))
    }
}
