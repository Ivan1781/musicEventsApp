package com.classic.event.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls

data class CountDto(
    @field:JsonProperty("All")
    val all: Int? = null,
    @field:JsonProperty("Date")
    val date: Int? = null,
)

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
    val isLastPage: Boolean? = null,
    @field:JsonProperty("Links")
    val links: List<Int>? = null,
)

data class EventOverviewDto(
    @field:JsonProperty("IdEventDate")
    val idEventDate: Long? = null,
    @field:JsonProperty("IdEventType")
    val idEventType: Int? = null,
    @field:JsonProperty("DetailLink")
    val detailLink: String? = null,
    @field:JsonProperty("EventType")
    val eventType: String? = null,
    @field:JsonProperty("DateFrom")
    val dateFrom: String? = null,
    @field:JsonProperty("DateTo")
    val dateTo: String? = null,
    @field:JsonProperty("DateTime4Overview")
    val dateTimeForOverview: String? = null,
    @field:JsonProperty("DateTime")
    val dateTime: String? = null,
    @field:JsonProperty("DateFromTo")
    val dateFromTo: String? = null,
    @field:JsonProperty("Title")
    val title: String? = null,
    @field:JsonProperty("ContentCategory")
    val contentCategory: String? = null,
    @field:JsonProperty("FormalCategory")
    val formalCategory: String? = null,
    @field:JsonProperty("isToday")
    val isToday: Any? = null,
    @field:JsonProperty("Duration")
    val duration: Int? = null,
    @field:JsonProperty("DurationTime")
    val durationTime: String? = null,
    @field:JsonProperty("City")
    val city: String? = null,
    @field:JsonProperty("Location")
    val location: String? = null,
    @field:JsonProperty("OrganizerName")
    val organizerName: String? = null,
    @field:JsonProperty("Longitude")
    val longitude: Double? = null,
    @field:JsonProperty("Latitude")
    val latitude: Double? = null,
    @field:JsonProperty("Status")
    val status: String? = null,
    @field:JsonProperty("OpusInfoShort")
    val opusInfoShort: String? = null,
    @field:JsonProperty("PriceUrl")
    val priceUrl: String? = null,
    @field:JsonProperty("PriceValueMinMax")
    val priceValueMinMax: String? = null,
    @field:JsonProperty("IsPublished")
    val isPublished: Boolean? = null,
    @field:JsonProperty("IsArchived")
    val isArchived: Boolean? = null,
    @field:JsonProperty("CreatedBy")
    val createdBy: String? = null,
    @field:JsonProperty("FlagStatusType")
    val flagStatusType: String? = null,
    @field:JsonProperty("AgeIndication")
    val ageIndication: String? = null,
)

data class DeutscheOperBerlinEventResponseDto(
    @field:JsonProperty("Count")
    val count: CountDto? = null,
    @field:JsonProperty("Pager")
    val pager: PagerDto? = null,
    @field:JsonProperty("EventOverview")
    @field:JsonSetter(nulls = Nulls.AS_EMPTY)
    val events: List<EventOverviewDto> = emptyList(),
)
