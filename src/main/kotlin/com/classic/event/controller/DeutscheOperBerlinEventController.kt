package com.classic.event.controller

import com.classic.event.dto.DeutscheOperBerlinEventResponseDto
import com.classic.event.service.DeutscheOperBerlinEventService
import com.classic.event.service.EventPersistenceService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class DeutscheOperBerlinEventController(
    private val eventService: DeutscheOperBerlinEventService,
    private val eventPersistenceService: EventPersistenceService
) {

    @GetMapping("/eventBerlin")
    fun persistData() {
        var page = 1
        for (a in 0..2) {
            val response = eventService.fetchEvents(pageNumber = page.toString())
            val events = eventService.mapToDtos(response)
            if (events.isNotEmpty()) {
                eventPersistenceService.saveAllFromDtos(events)
            }
            page += 1
        }
//         while (response.pager?.isLastPage == false)
    }

    fun fetchEvents(pageNumber: String = "1"
    ): ResponseEntity<DeutscheOperBerlinEventResponseDto> {
        return ResponseEntity.ok(eventService.fetchEvents(pageNumber = pageNumber))
    }
}
