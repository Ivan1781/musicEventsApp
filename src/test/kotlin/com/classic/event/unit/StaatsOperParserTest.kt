package com.classic.event.unit

import com.classic.event.dto.StaatsOperBerlinEventResponseDto
import com.classic.event.parsers.StaatsOperBerlinHtmlParser
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import testUtils.getElements
import utils.loadFile
import java.time.LocalDate
import java.time.LocalDateTime

@SpringBootTest
class StaatsOperParserTest {

    @Test
    fun `parsing staatsoper berlin verification`() {
        val responseMock = loadFile("mock/berlinStaatsOper.txt")

        val ev = StaatsOperBerlinHtmlParser.parse(responseMock)

        val events = getElements("article", responseMock)
        assertThat(events.size).isEqualTo(getEvents(ev))

        val firstEvent = ev.days.first()
        assertThat(firstEvent.date).isEqualTo(LocalDate.parse("2025-12-16"))
        assertThat(firstEvent.events).hasSize(1)
        val firstEventItem = firstEvent.events.first()
        assertThat(firstEventItem.dateTime).isEqualTo(LocalDateTime.parse("2025-12-16T20:00"))
        assertThat(firstEventItem.venueName).isEqualTo("Apollosaal")
        assertThat(firstEventItem.title).isEqualTo("Konzert des Jugendchors der Staatsoper")
        assertThat(firstEventItem.detailUrl).isEqualTo(
            "/de/veranstaltungen/konzert-des-jugendchors-der-staatsoper.22258/#event-124815"
        )
        assertThat(firstEventItem.workInfo).isNull()
        assertThat(firstEventItem.duration).isNull()
        assertThat(firstEventItem.ticketUrl).isEqualTo(
            "https://staatsoper-berlin.eventim-inhouse.de/webshop/webticket/shop?language=de&event=19259"
        )
        assertThat(firstEventItem.priceText).isEqualTo("15")
        assertThat(firstEventItem.category).isNull()
        assertThat(firstEventItem.city).isEqualTo("Berlin")

        val lastEvent = ev.days.last()
        assertThat(lastEvent.date).isEqualTo(LocalDate.parse("2025-12-22"))
        assertThat(lastEvent.events).hasSize(1)
        val lastEventItem = lastEvent.events.first()
        assertThat(lastEventItem.dateTime).isEqualTo(LocalDateTime.parse("2025-12-22T18:00"))
        assertThat(lastEventItem.venueName).isEqualTo("Apollosaal")
        assertThat(lastEventItem.title).isEqualTo("Weihnachtskonzert Kinderchor der Staatsoper")
        assertThat(lastEventItem.detailUrl).isEqualTo(
            "/de/veranstaltungen/weihnachtskonzert-kinderchor-der-staatsoper.22259/#event-124816"
        )
        assertThat(lastEventItem.workInfo).isNull()
        assertThat(lastEventItem.duration).isNull()
        assertThat(lastEventItem.ticketUrl).isNull()
        assertThat(lastEventItem.priceText).isEqualTo("ausverkauft")
        assertThat(lastEventItem.category).isNull()
        assertThat(lastEventItem.city).isEqualTo("Berlin")

    }

    private fun getEvents(ev: StaatsOperBerlinEventResponseDto): Int {
        return ev
            .days
            .sumOf { it.events.size }
    }
}
