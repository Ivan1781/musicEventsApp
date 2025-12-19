package com.classic.event.parsers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.classic.event.dto.DresdenSemperOperEventDto
import com.classic.event.dto.DresdenSemperOperEventListDto
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

object SemperOperDresdenParser {

    private val mapper = jacksonObjectMapper()

    fun parse(body: String): DresdenSemperOperEventListDto {
        val htmlFragments = parseJsonBody(body)
        val events = htmlFragments.mapNotNull { parseEvent(it) }
        return DresdenSemperOperEventListDto(events)
    }

    private fun parseJsonBody(body: String): List<String> {
        val sanitized = body.replace("\n", "").replace("\r", "")
        val htmlByKey: Map<String, String> = mapper.readValue(sanitized)
        return htmlByKey
            .entries
            .sortedBy { it.key.toIntOrNull() }
            .map { it.value }
    }

    private fun parseEvent(html: String): DresdenSemperOperEventDto? {
        val document = Jsoup.parse(html)
        val container = document.selectFirst("div.frame.filter-frame-element.frame-spielplan") ?: return null
        val headerLink = container.selectFirst("h2.ni-header.order-1 a")

        return DresdenSemperOperEventDto(
            category = container.textFrom("span.ni-event-category"),
            detailUrl = headerLink?.attr("href")?.trim().orEmpty(),
            title = headerLink?.text()?.trim().orEmpty(),
            author = container.textFrom("div.order-2.ni-subtitle"),
            dateTime = container.selectFirst("time.sr-only")?.attr("datetime")?.trim().orEmpty(),
            status = container.textFrom("span.so_pricerange"),
            city = "Dresden"
        )
    }

    private fun Element.textFrom(selector: String): String {
        return this.selectFirst(selector)?.text()?.trim().orEmpty()
    }
}