package com.classic.event.unit

import com.classic.event.dto.StaatsOperBerlinEventResponseDto
import com.classic.event.parsers.StaatsOperBerlinHtmlParser
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import testUtils.getElements
import utils.loadFile

@SpringBootTest
class StaatsOperParserTest {

    @Test
    fun `parsing staatsoper berlin verification`() {
        val responseMock = loadFile("mock/berlinStaatsOper.txt")

        val ev = StaatsOperBerlinHtmlParser.parse(responseMock)

        val events = getElements("article", responseMock)
        assertThat(events.size).isEqualTo(getEvents(ev))

        val event = events.first()
    }

    private fun getEvents(ev: StaatsOperBerlinEventResponseDto): Int {
        return ev
            ?.days
            ?.sumOf { it.events.size }
            ?: 0
    }
}
