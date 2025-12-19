package com.classic.event.dto

import java.time.LocalDateTime
import com.classic.event.entity.EventEntity as EventEntity
import java.time.format.DateTimeFormatter

data class EventDto(
    val title: String?,
    val city: String?,
    val detailUrl: String?,
    val dateTime: String?,
    val duration: String?,
    val location: String?,
    val price: String?,
    val ticketUrl: String?,
    val category: String?,
    val author: String?
) {
    fun toEntity(): EventEntity =
        EventEntity(
            title = title,
            city = city,
            detailUrl = detailUrl,
            dateTime = LocalDateTime.parse(dateTime.toString()),
            duration = duration,
            location = location,
            price = price,
            ticketUrl = ticketUrl,
            category =  category,
            author = author
        )
}

fun EventEntity.toDto(): EventDto =
    EventDto(
        title = title,
        city = city,
        detailUrl = detailUrl,
        dateTime = dateTime?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
        duration = duration,
        location = location,
        price = price,
        ticketUrl = ticketUrl,
        category = category,
        author = author
    )
