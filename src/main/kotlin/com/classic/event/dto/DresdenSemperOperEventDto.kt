package com.classic.event.dto

import com.classic.event.entity.EventEntity
import constants.DefaultCities
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
    val price: String,
    val city: String,
    val location: String
)

private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

private fun eventDtoToEntity(dto: DresdenSemperOperEventDto): EventEntity {
    val parsedDateTime = dto.dateTime.trim()
        .takeIf { it.isNotEmpty() }
        ?.let { LocalDateTime.parse(it, dateTimeFormatter) }

    return EventEntity(
        category = dto.category,
        detailUrl = dto.detailUrl,
        title = dto.title,
        author = dto.author,
        dateTime = parsedDateTime,
        price = dto.price,
        location = dto.location,
        city = DefaultCities.DRESDEN
    )
}
