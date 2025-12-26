package com.classic.event.controller

import com.classic.event.dto.StaatsOperBerlinEventResponseDto
import com.classic.event.entity.EventEntity
import com.classic.event.service.EventPersistenceService
import com.classic.event.service.StaatsOperBerlinEventService
import java.time.LocalDate
import org.springframework.http.ResponseEntity
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.format.DateTimeFormatter
import kotlin.math.log

@RestController
class StaatsOperBerlinEventController(
    private val eventService: StaatsOperBerlinEventService,
    private val eventPersistenceService: EventPersistenceService
) {

    @Scheduled(cron = "0 0 2 * * ?")
    @GetMapping("/save")
    fun syncEventsDaily() {
        try {
            var date = LocalDate.now().withDayOfMonth(1)

            val entities = mutableListOf<EventEntity>()
            for (a in 0..5) {
                val response = this.fetchEvents(date)
                val body = response.body!!
                entities.addAll(eventService.mapToEvents(body))
                date = LocalDate.parse(
                    body.nextPageUrl!!.split("/").last(), DateTimeFormatter.ofPattern("dd-MM-yyyy")
                )
            }
            if (entities.isNotEmpty()) {
                eventPersistenceService.syncEvents(entities)
            }
        } catch (e: Exception) {
            // retry
        }
    }



    private fun fetchEvents(date: LocalDate): ResponseEntity<StaatsOperBerlinEventResponseDto> {
        return ResponseEntity.ok(eventService.fetchEvents(date))
    }
}
