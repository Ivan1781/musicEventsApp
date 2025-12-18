package com.classic.event.service

import com.classic.event.dto.EventDto as EventDto
import com.classic.event.dto.toDto
import com.classic.event.entity.EventEntity
import com.classic.event.repository.EventRepository
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EventPersistenceService(
    private val eventRepository: EventRepository
) {
    companion object {
        private val logger = LoggerFactory.getLogger(EventPersistenceService::class.java)
    }

    @Transactional
    fun save(event: EventEntity): EventEntity = eventRepository.save(event)

    fun saveAll(events: List<EventEntity>): List<EventEntity> =
        events.fold(mutableListOf()) { saved, entity ->
            try {
                saved += eventRepository.saveAndFlush(entity)
            } catch (_: DataIntegrityViolationException) {
                logger.info("The events {} exists in data base", entity)
            }
            saved
        }

    fun saveAllFromDtos(events: List<EventDto>): List<EventDto> =
        events.fold(mutableListOf()) { saved, dto ->
            val entity = dto.toEntity()
            try {
                saved += eventRepository.saveAndFlush(entity).toDto()
            } catch (_: DataIntegrityViolationException) {
                logger.info("The events {} exists in2data2base", dto)
            }
            saved
        }
}
