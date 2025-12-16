package com.classic.event.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls
import constants.DatePattern.DATE_DOT_DMY
import constants.DatePattern.HOUR_MINUTE
import constants.DefaultCities
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class CountDto(
    @field:JsonProperty("All")
    val all: Int? = null,
    @field:JsonProperty("Date")
    val date: Int? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class PagerDto(
    @field:JsonProperty("NbResults")
    val nbResults: Int? = null,
    @field:JsonProperty("FirstIndex")
    val firstIndex: Int? = null,
    @field:JsonProperty("LastIndex")
    val lastIndex: Int? = null,
    @field:JsonProperty("IsFirstPage")
    val isFirstPage: Boolean? = null,
    @field:JsonProperty("IsLastPage")
    val isLastPage: Boolean? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class EventOverviewDto(
    @field:JsonProperty("DetailLink")
    val detailLink: String? = null,
    @field:JsonProperty("DateTime4Overview")
    val dateTimeForOverview: String,
    @field:JsonProperty("Title")
    val title: String? = null,
    @field:JsonProperty("ContentCategory")
    val contentCategory: String? = null,
    @field:JsonProperty("Duration")
    val duration: Int? = null,
    @field:JsonProperty("City")
    val city: String? = null,
    @field:JsonProperty("Location")
    val location: String? = null,
    @field:JsonProperty("Status")
    val status: String? = null,
    @field:JsonProperty("PriceUrl")
    val priceUrl: String? = null,
    @field:JsonProperty("PriceValueMinMax")
    val priceValueMinMax: String? = null,
    @field:JsonProperty("IsPublished")
    val isPublished: Boolean? = null,
    @field:JsonProperty("IsArchived")
    val isArchived: Boolean? = null,
    @field:JsonProperty("AgeIndication")
    val ageIndication: String? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class DeutscheOperBerlinEventResponseDto(
    @field:JsonProperty("Count")
    val count: CountDto? = null,
    @field:JsonProperty("Pager")
    val pager: PagerDto? = null,
    @field:JsonProperty("EventOverview")
    @field:JsonSetter(nulls = Nulls.AS_EMPTY)
    val events: List<EventOverviewDto> = emptyList()
) {
    fun toEvents(): List<EventDto> = events.map { it.toEvent() }
}

fun EventOverviewDto.toEvent(): EventDto =
    EventDto(
        title = title,
        city = city?.trim()?.takeIf { it.isNotEmpty() } ?: DefaultCities.BERLIN,
        detailUrl = detailLink,
        dateTime = transformDate(dateTimeForOverview).toString(),
        duration = duration?.toString(),
        location = location,
        price = priceValueMinMax,
        ticketUrl = priceUrl
    )

private fun transformDate(input: String): LocalDateTime {
    val (datePart, timePart) = input
        .substringAfter(" ")
        .split(" - ")
        .map(String::trim)
        .also { require(it.size == 2) { "Expected date and time separated by ' - '" } }
        .let { it[0] to it[1] }

    val date = LocalDate.parse(datePart, DATE_DOT_DMY)
    val time = LocalTime.parse(timePart, HOUR_MINUTE)

    return LocalDateTime.of(date, time.withSecond(0))
}