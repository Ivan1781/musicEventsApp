package com.classic.event.controller

import com.classic.event.dto.DresdenSemperOperEventListDto
import com.classic.event.service.DeutscheOperBerlinEventService
import com.classic.event.service.EventPersistenceService
import com.classic.event.service.SemperOperDresdenEventService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class SemperOperDresdenEventController(
    private val eventService: SemperOperDresdenEventService,
    private val eventPersistenceService: EventPersistenceService) {

    @GetMapping("/semper")
    fun preserveData() {
        var eventNumber = 1
//        for (a in 0..2) {
            val events = eventService.fetchEvents(eventNumber).toEvents()
            if (events.isNotEmpty()) {
                eventPersistenceService.saveAll(events)
//            }
//            eventNumber += 1
        }

    }
}