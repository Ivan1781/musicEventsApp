package com.classic.event.parsers

import com.classic.event.dto.StaatsOperBerlinDayDto
import com.classic.event.dto.StaatsOperBerlinEventDto
import com.classic.event.dto.StaatsOperBerlinEventResponseDto
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
object StaatsOperBerlinHtmlParser {
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    fun parse(html: String): StaatsOperBerlinEventResponseDto {
        val document = Jsoup.parse(html)
        val monthTitle = document.selectFirst("div.spielplan-month-title h2")?.text()?.trim().orEmpty()
        val date = document.selectFirst(".js-pagination #next-link")?.attr("data-pagination-fragment-url")?.takeIf { it.isNotBlank() }

        val regex = Regex("\\d{2}-\\d{2}-\\d{4}")
        val nextDate = regex.find(date.toString())?.value

        val days = document.select("div.spielplan-day").mapNotNull { parseDay(it) }

        return StaatsOperBerlinEventResponseDto(
            monthTitle = monthTitle,
            days = days,
            nextPageUrl = nextDate
        )
    }

    private fun parseDay(dayElement: Element): StaatsOperBerlinDayDto? {
        val events = dayElement.select("article.termin-list__item").mapNotNull { parseEvent(it) }
        val date = events.firstOrNull()?.dateTime?.toLocalDate()
        return if (events.isEmpty() || date == null) {
            null
        } else {
            StaatsOperBerlinDayDto(
                date = date,
                events = events,
            )
        }
    }

    private fun parseEvent(article: Element): StaatsOperBerlinEventDto? {
        val timeElement = article.selectFirst("time[datetime]") ?: return null
        val dateTime = parseDateTime(timeElement.attr("datetime")) ?: return null

        val venueLink = article.selectFirst(".termin__spielstaette a")
        val detailLink = article.selectFirst(".termin__title a")

        return StaatsOperBerlinEventDto(
            dateTime = dateTime,
            venueName = venueLink?.text().clean(),
            title = detailLink?.text()?.trim().orEmpty(),
            detailUrl = detailLink?.attr("href")?.trim().orEmpty(),
            workInfo = article.selectFirst(".termin__werkinfo")?.text().clean(),
            duration = article.selectFirst(".termin__spieldauer")?.text().clean(),
            ticketUrl = article.selectFirst(".termin__ticket-button a")?.attr("href")?.clean(),
            priceText = article.selectFirst(".termin__preisinformation")?.text().clean(),
            category = null
        )
    }

    private fun parseDateTime(raw: String): LocalDateTime? =
        runCatching { LocalDateTime.parse(raw.trim(), dateTimeFormatter) }.getOrNull()

    private fun String?.clean(): String? =
        this
            ?.replace(Regex("\\s+"), " ")
            ?.trim()
            ?.takeIf { it.isNotEmpty() }
}