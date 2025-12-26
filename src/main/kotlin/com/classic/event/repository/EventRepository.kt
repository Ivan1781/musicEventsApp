package com.classic.event.repository

import com.classic.event.entity.EventEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EventRepository : JpaRepository<EventEntity, Long> {
    fun findByDetailUrlIn(detailUrls: List<String>): List<EventEntity>
}
