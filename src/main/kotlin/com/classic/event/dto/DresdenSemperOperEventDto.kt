package com.classic.event.dto

import com.classic.event.entity.EventEntity
import constants.DefaultCities
import java.time.LocalDateTime

data class DresdenSemperOperEventListDto(
    val events: List<DresdenSemperOperEventDto>
) {
    fun toEvents(): List<EventEntity> {
        return events.map { eventDtoToEntity(it) }
    }
}

data class DresdenSemperOperEventDto(
    val category: String,
    val detailUrl: String,
    val title: String,
    val author: String,
    val dateTime: String,
    val status: String,
    val city: String
)

private fun eventDtoToEntity(dto: DresdenSemperOperEventDto): EventEntity {
    return EventEntity(
        category = dto.category,
        detailUrl = dto.detailUrl,
        title = dto.title,
        author = dto.author,
        dateTime = LocalDateTime.parse(dto.dateTime),
        city = DefaultCities.DRESDEN
    )
}

