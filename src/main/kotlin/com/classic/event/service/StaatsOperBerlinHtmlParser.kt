package com.classic.event.service

import com.classic.event.dto.StaatsOperBerlinDayDto
import com.classic.event.dto.StaatsOperBerlinEventDto
import com.classic.event.dto.StaatsOperBerlinEventResponseDto
import com.classic.event.dto.StaatsOperBerlinPerformerDto
import com.classic.event.dto.StaatsOperBerlinProgramItemDto
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.springframework.stereotype.Component

@Component
class StaatsOperBerlinHtmlParser {
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    fun parse(html: String): StaatsOperBerlinEventResponseDto {
        val document = Jsoup.parse(html)
        val monthTitle = document.selectFirst("div.spielplan-month-title h2")?.text()?.trim().orEmpty()
        val nextLink = document.selectFirst(".js-pagination #next-link")?.attr("data-pagination-fragment-url")?.takeIf { it.isNotBlank() }

        val days = document.select("div.spielplan-day").mapNotNull { parseDay(it) }

        return StaatsOperBerlinEventResponseDto(
            monthTitle = monthTitle,
            days = days,
            nextPageUrl = nextLink,
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
        val id = article.attr("data-termin-id").toLongOrNull() ?: return null

        val timeElement = article.selectFirst("time[datetime]") ?: return null
        val dateTime = parseDateTime(timeElement.attr("datetime")) ?: return null
        val timeSpans = timeElement.select("span").map { it.text().trim() }

        val venueLink = article.selectFirst(".termin__spielstaette a")
        val detailLink = article.selectFirst(".termin__title a")

        val tags = article.select(".termin__kalendarien li").mapNotNull { it.text().clean() }

        val performers = article.select(".besetzung__item").flatMap { performerItem ->
            val role = performerItem.selectFirst(".besetzung__rolle")?.text().clean()
            performerItem.select(".besetzung__beteiligte-liste-item").mapNotNull { participant ->
                val name = participant.text().clean() ?: return@mapNotNull null
                val profileUrl = participant.selectFirst("a")?.attr("href")?.clean()
                StaatsOperBerlinPerformerDto(role = role, name = name, profileUrl = profileUrl)
            }
        }

        val program = article.select(".konzert-programmpunkt").map { programItem ->
            StaatsOperBerlinProgramItemDto(
                artist = programItem.selectFirst(".konzert-programmpunkt__kuenstler")?.text().clean(),
                piece = programItem.selectFirst(".konzert-programmpunkt__titel")?.text().clean(),
            )
        }

        return StaatsOperBerlinEventDto(
            id = id,
            dateTime = dateTime,
            weekdayLabel = timeSpans.getOrNull(0),
            dateLabel = timeSpans.getOrNull(1),
            timeLabel = timeSpans.getOrNull(2),
            venueName = venueLink?.text().clean(),
            venueUrl = venueLink?.attr("href")?.clean(),
            title = detailLink?.text()?.trim().orEmpty(),
            detailUrl = detailLink?.attr("href")?.trim().orEmpty(),
            workInfo = article.selectFirst(".termin__werkinfo")?.text().clean(),
            tags = tags,
            bookingNote = article.selectFirst(".termin__buchungshinweis")?.text().clean(),
            duration = article.selectFirst(".termin__spieldauer")?.text().clean(),
            ticketUrl = article.selectFirst(".termin__ticket-button a")?.attr("href")?.clean(),
            priceText = article.selectFirst(".termin__preisinformation")?.text().clean(),
            performers = performers,
            program = program,
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
