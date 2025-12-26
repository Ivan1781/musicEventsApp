package com.classic.event.service

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

    private val logger = LoggerFactory.getLogger(EventPersistenceService::class.java)


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

    @Transactional
    fun syncEvents(fetchedEvents: List<EventEntity>) {
        if (fetchedEvents.isEmpty()) {
            logger.info("No events to sync")
            return
        }

        val detailUrls = fetchedEvents.mapNotNull { it.detailUrl }
        val existingEventsMap = eventRepository.findByDetailUrlIn(detailUrls)
            .associateBy { it.detailUrl }

        val toInsert = mutableListOf<EventEntity>()
        val toUpdate = mutableListOf<EventEntity>()

        fetchedEvents.forEach { fetchedEvent ->
            val existingEvent = existingEventsMap[fetchedEvent.detailUrl]

            when {
                existingEvent == null -> {
                    toInsert.add(fetchedEvent)
                }
                hasChanges(fetchedEvent, existingEvent) -> {
                    toUpdate.add(fetchedEvent.copy(id = existingEvent.id))
                }
                else -> {
                    logger.debug("Skipping unchanged event: ${fetchedEvent.detailUrl}")
                }
            }
        }

        if (toInsert.isNotEmpty()) {
            eventRepository.saveAll(toInsert)
            logger.info("Inserted ${toInsert.size} new events")
        }

        if (toUpdate.isNotEmpty()) {
            eventRepository.saveAll(toUpdate)
            logger.info("Updated ${toUpdate.size} changed events")
        }

        logger.info("Sync complete: ${toInsert.size} inserted, ${toUpdate.size} updated, " +
                "${fetchedEvents.size - toInsert.size - toUpdate.size} unchanged")
    }

    private fun hasChanges(fetched: EventEntity, existing: EventEntity): Boolean {
        return fetched.calculateHash() != existing.calculateHash()
    }
}
