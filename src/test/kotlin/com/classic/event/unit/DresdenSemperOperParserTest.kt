package com.classic.event.unit

import com.classic.event.parsers.SemperOperDresdenParser
import org.jsoup.Jsoup
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import utils.loadFile

class DresdenSemperOperParserTest {

    @Test
    fun `parse semper oper events`() {
        val body = loadFile("mock/dresdenSemperOper.txt")
        val document = Jsoup.parse(body).body().select(".frame-spielplan").map { it.toString() }
        val result = SemperOperDresdenParser.parse(document)

        Assertions.assertEquals(63, result.events.size)

        result.events.forEach { println("-----" + it) }


        val first = result.events.first()
        Assertions.assertEquals("Opera", first.category)
        Assertions.assertEquals(
            "https://www.semperoper.de/en/whats-on/schedule/stid/en-zauberfloete/61859.html#a_32508",
            first.detailUrl
        )
        Assertions.assertEquals("Die Zauberflöte / The Magic Flute", first.title)
        Assertions.assertEquals("Wolfgang Amadeus Mozart", first.author)
        Assertions.assertEquals("2025-12-20 14:00:00", first.dateTime)
        Assertions.assertEquals("Tickets", first.status)


        val last = result.events.last()
        Assertions.assertEquals("Opera", last.category)
        Assertions.assertEquals(
            "https://www.semperoper.de/en/whats-on/schedule/stid/en-zauberfloete/61859.html#a_32560",
            last.detailUrl
        )
        Assertions.assertEquals("Die Zauberflöte / The Magic Flute", last.title)
        Assertions.assertEquals("Wolfgang Amadeus Mozart", last.author)
        Assertions.assertEquals("2026-02-11 19:00:00", last.dateTime)
        Assertions.assertEquals("Tickets", last.status)
    }
}