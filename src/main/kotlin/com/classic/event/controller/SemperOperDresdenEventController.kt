package com.classic.event.controller

import com.classic.event.entity.EventEntity
import com.classic.event.parsers.SemperOperDresdenParser
import com.classic.event.service.EventPersistenceService
import com.classic.event.service.SemperOperDresdenEventService
import com.classic.event.service.SemperoperScraperService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class SemperOperDresdenEventController(
    private val eventPersistenceService: EventPersistenceService,
    private val semperoperScraperService: SemperoperScraperService
    ) {

    @GetMapping("/semper")
    fun preserveData(): ResponseEntity<List<EventEntity>> {
//        var eventNumber = 1
//
//            val events = eventService.fetchEvents(eventNumber).toEvents()
//            if (events.isNotEmpty()) {
//                eventPersistenceService.saveAll(events)
//
//        }
        val e = semperoperScraperService.loadEvents()
        val dto = SemperOperDresdenParser.parse(e)
        val r = eventPersistenceService.saveAll(dto.toEvents())
        return ResponseEntity.ok(r)
    }
}
