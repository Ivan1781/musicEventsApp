package com.classic.event.parsers

import com.classic.event.dto.DresdenSemperOperEventDto
import com.classic.event.dto.DresdenSemperOperEventListDto
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object SemperOperDresdenParser {
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    fun parse(body: List<String>): DresdenSemperOperEventListDto {
        val events = body.map { parseEvent(it) }
        return DresdenSemperOperEventListDto(events)
    }

    private fun parseEvent(htmlEvent: String): DresdenSemperOperEventDto {
        val root = Jsoup.parseBodyFragment(htmlEvent).body()
        val headerLink = root.selectFirst("h2.ni-header.order-1 a")
        val priceInfo = root.selectFirst(".ni-event-pricecategory [aria-hidden=true]")?.text()?.trim() ?:
            root.selectFirst(".so_pricerange")?.text()?.trim().orEmpty()
        val rawDateTime = root.selectFirst("time.sr-only")?.attr("datetime")?.trim().orEmpty()
        val formattedDateTime = formatDateTime(rawDateTime)

        return DresdenSemperOperEventDto(
            category = root.textFrom("span.ni-event-category"),
            detailUrl = headerLink?.attr("href")?.trim().orEmpty(),
            title = headerLink?.text()?.trim().orEmpty(),
            author = root.textFrom("div.order-2.ni-subtitle"),
            dateTime = formattedDateTime,
            status = root.textFrom("span.so_pricerange"),
            price = priceInfo,
            location = root.textFrom(".ni-event-venue [aria-hidden=true]"),
            city = "Dresden"
        )
    }

    private fun Element.textFrom(selector: String): String {
        return this.selectFirst(selector)?.text()?.trim().orEmpty()
    }

    private fun formatDateTime(raw: String): String {
        if (raw.isBlank()) return ""
        return runCatching { LocalDateTime.parse(raw).format(dateTimeFormatter) }
            .getOrElse { raw }
    }
}
