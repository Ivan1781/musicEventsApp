package com.classic.event.dto

import com.classic.event.entity.EventEntity
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import constants.DefaultCities
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

typealias HannoverStaatsTheaterEventResponseDto = EventResponseDto<HannoverStaatsTheaterEventOverviewDto>

@JsonIgnoreProperties(ignoreUnknown = true)
data class HannoverStaatsTheaterEventOverviewDto(
    @field:JsonProperty("Slug")
    val slug: String? = null,
    @field:JsonProperty("Title")
    override val title: String? = null,
    @field:JsonProperty("ContentCategory")
    override val contentCategory: String? = null,
    @field:JsonProperty("DateDay")
    val dateDay: String? = null,
    @field:JsonProperty("DateMonthYear")
    val dateMonthYear: String? = null,
    @field:JsonProperty("DateTimeStart")
    val dateTimeStart: String? = null,
    @field:JsonProperty("DateTimeEnd")
    val dateTimeEnd: String? = null,
    @field:JsonProperty("Duration")
    override val duration: Int? = null,
    @field:JsonProperty("DurationTime")
    val durationTime: String? = null,
    @field:JsonProperty("City")
    override val city: String? = null,
    @field:JsonProperty("Location")
    override val location: String? = null,
    @field:JsonProperty("Status")
    override val status: String? = null,
    @field:JsonProperty("OpusInfo")
    override val opusInfo: String? = null,
    @field:JsonProperty("OpusInfoShort")
    val opusInfoShort: String? = null,
    @field:JsonProperty("PriceUrl")
    override val priceUrl: String? = null,
    @field:JsonProperty("PriceValueMinMax")
    override val priceValueMinMax: String? = null,
    @field:JsonProperty("IsPublished")
    val isPublished: Int? = null,
    @field:JsonProperty("IsArchived")
    val isArchived: Int? = null,
    @field:JsonProperty("AgeIndication")
    override val ageIndication: String? = null
) : EventOverviewBase

private val hannoverDateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.GERMAN)
private val hannoverTimeFormatter = DateTimeFormatter.ofPattern("H:mm")

fun HannoverStaatsTheaterEventResponseDto.toEvents(): List<EventEntity> =
    events.map { it.toEvent() }

fun HannoverStaatsTheaterEventOverviewDto.toEvent(): EventEntity =
    toEventEntity(
        detailUrl = "https://staatstheater-hannover.de" + slug,
        dateTime = parseDateTime(),
        defaultCity = DefaultCities.HANNOVER
    )

private fun HannoverStaatsTheaterEventOverviewDto.buildDetailUrl(baseUrl: String): String? {
    val rawSlug = slug?.trim()?.takeIf { it.isNotEmpty() } ?: return null
    return if (rawSlug.startsWith("http", ignoreCase = true)) {
        rawSlug
    } else {
        "${baseUrl.trimEnd('/')}/${rawSlug.trimStart('/')}"
    }
}

private fun HannoverStaatsTheaterEventOverviewDto.parseDateTime(): LocalDateTime? {
    val day = dateDay?.trim()?.takeIf { it.isNotEmpty() } ?: return null
    val monthYear = dateMonthYear?.trim()?.takeIf { it.isNotEmpty() } ?: return null
    val time = dateTimeStart?.trim()?.takeIf { it.isNotEmpty() } ?: return null

    val date = runCatching { LocalDate.parse("$day $monthYear", hannoverDateFormatter) }.getOrNull() ?: return null
    val localTime = runCatching { LocalTime.parse(time, hannoverTimeFormatter) }.getOrNull() ?: return null

    return LocalDateTime.of(date, localTime)
}
