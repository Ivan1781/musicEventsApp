package com.classic.event.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls

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
data class EventResponseDto<T>(
    @field:JsonProperty("Count")
    val count: CountDto? = null,
    @field:JsonProperty("Pager")
    val pager: PagerDto? = null,
    @field:JsonProperty("EventOverview")
    @field:JsonSetter(nulls = Nulls.AS_EMPTY)
    val events: List<T> = emptyList()
)
