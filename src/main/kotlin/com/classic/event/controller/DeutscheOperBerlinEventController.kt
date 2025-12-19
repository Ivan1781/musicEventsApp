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
            val events = eventService.fetchEvents(pageNumber = page.toString()).toEvents()
            if (events.isNotEmpty()) {
                eventPersistenceService.saveAllFromDtos(events)
            }
            page += 1
        }
//         while (response.pager?.isLastPage == false)
    }
}
