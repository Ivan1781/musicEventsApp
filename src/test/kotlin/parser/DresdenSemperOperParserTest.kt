package parser

import com.classic.event.parsers.SemperOperDresdenParser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import utils.loadFile

class DresdenSemperOperParserTest {

    @Test
    fun `parse events from mock file`() {
        val body = loadFile("mock/dresdenSemperOper.txt")

        val result = SemperOperDresdenParser.parse(body)

        assertEquals(11, result.events.size)

        val first = result.events.first()
        assertEquals("Ballet for kids", first.category)
        assertEquals("https://www.semperoper.de/education/monsieur-petipa.html", first.detailUrl)
        assertEquals("With your permission, Monsieur Petipa!", first.title)
        assertEquals("", first.author)
        assertEquals("2026-01-27T10:00:00", first.dateTime)
        assertEquals("", first.status)


        val last = result.events.last()
        assertEquals("Extra", last.category)
        assertEquals("https://www.semperoper.de/die-semperoper/semperopernball.html", last.detailUrl)
        assertEquals("SemperOpernball", last.title)
        assertEquals("", last.author)
        assertEquals("2026-02-06T20:00:00", last.dateTime)
        assertEquals("", last.status)
    }
}
