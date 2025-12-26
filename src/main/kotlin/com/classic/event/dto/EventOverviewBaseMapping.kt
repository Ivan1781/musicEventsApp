package com.classic.event.dto

import com.classic.event.entity.EventEntity
import java.time.LocalDateTime

fun EventOverviewBase.toEventEntity(
    detailUrl: String?,
    dateTime: LocalDateTime?,
    defaultCity: String
): EventEntity =
    EventEntity(
        title = title,
        city = city?.trim()?.takeIf { it.isNotEmpty() } ?: defaultCity,
        detailUrl = detailUrl,
        dateTime = dateTime,
        duration = duration?.toString(),
        location = location,
        price = priceValueMinMax,
        ticketUrl = priceUrl,
        category = contentCategory,
        author = opusInfo
    )
