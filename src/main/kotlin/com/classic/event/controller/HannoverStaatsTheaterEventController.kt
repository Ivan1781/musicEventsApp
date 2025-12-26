package com.classic.event.controller

import com.classic.event.dto.toEvents
import com.classic.event.service.EventPersistenceService
import com.classic.event.service.HannoverStaatsTheaterEventService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HannoverStaatsTheaterEventController(
    private val eventService: HannoverStaatsTheaterEventService,
    private val eventPersistenceService: EventPersistenceService
) {

    @GetMapping("/eventHannover")
    fun persistData() {
        var page = 1
        for (a in 0..2) {
            val events = eventService.fetchEvents(pageNumber = page.toString()).toEvents()
            if (events.isNotEmpty()) {
                eventPersistenceService.saveAll(events)
            }
            page += 1
        }
    }
}
