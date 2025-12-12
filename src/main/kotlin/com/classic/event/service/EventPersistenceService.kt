package com.classic.event.service

import com.classic.event.dto.EventDto as EventDto
import com.classic.event.dto.toDto
import com.classic.event.entity.EventEntity
import com.classic.event.repository.EventRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EventPersistenceService(
    private val eventRepository: EventRepository
) {
    @Transactional
    fun save(event: EventEntity): EventEntity = eventRepository.save(event)

    @Transactional
    fun saveAll(events: List<EventEntity>): List<EventEntity> = eventRepository.saveAll(events)

    @Transactional
    fun saveAllFromDtos(events: List<EventDto>): List<EventDto> =
        eventRepository.saveAll(events.map { it.toEntity() }).map { it.toDto() }
}
