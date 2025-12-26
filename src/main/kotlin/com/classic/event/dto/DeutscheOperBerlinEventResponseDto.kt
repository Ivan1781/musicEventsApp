package com.classic.event.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.classic.event.entity.EventEntity
import constants.DatePattern.DATE_DOT_DMY
import constants.DatePattern.HOUR_MINUTE
import constants.DefaultCities
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

typealias DeutscheOperBerlinEventResponseDto = EventResponseDto<BerlinDeutscheOperEventOverviewDto>

@JsonIgnoreProperties(ignoreUnknown = true)
data class BerlinDeutscheOperEventOverviewDto(
    @field:JsonProperty("DetailLink")
    val detailLink: String? = null,
    @field:JsonProperty("DateTime4Overview")
    val dateTimeForOverview: String,
    @field:JsonProperty("Title")
    override val title: String? = null,
    @field:JsonProperty("ContentCategory")
    override val contentCategory: String? = null,
    @field:JsonProperty("Duration")
    override val duration: Int? = null,
    @field:JsonProperty("City")
    override val city: String? = null,
    @field:JsonProperty("Location")
    override val location: String? = null,
    @field:JsonProperty("Status")
    override val status: String? = null,
    @field:JsonProperty("PriceUrl")
    override val priceUrl: String? = null,
    @field:JsonProperty("PriceValueMinMax")
    override val priceValueMinMax: String? = null,
    @field:JsonProperty("IsPublished")
    val isPublished: Boolean? = null,
    @field:JsonProperty("IsArchived")
    val isArchived: Boolean? = null,
    @field:JsonProperty("AgeIndication")
    override val ageIndication: String? = null,
    @field:JsonProperty("OpusInfoShort")
    override val opusInfo: String? = null
) : EventOverviewBase

fun DeutscheOperBerlinEventResponseDto.toEvents(): List<EventEntity> =
    events.map { it.toEvent() }

fun BerlinDeutscheOperEventOverviewDto.toEvent(): EventEntity =
    toEventEntity(
        detailUrl = "https://deutscheoperberlin.de" + detailLink,
        dateTime = transformDate(dateTimeForOverview),
        defaultCity = DefaultCities.BERLIN
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
