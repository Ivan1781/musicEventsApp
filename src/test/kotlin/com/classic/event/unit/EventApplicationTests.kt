package com.classic.event.unit

import com.classic.event.controller.DeutscheOperBerlinEventController
import com.classic.event.controller.StaatsOperBerlinEventController
import com.classic.event.dto.DeutscheOperBerlinEventResponseDto
import com.classic.event.dto.StaatsOperBerlinEventResponseDto
import com.classic.event.dto.toEvents
import com.classic.event.service.RemoteSiteService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.ResponseEntity
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import testUtils.getElements
import utils.getResource
import utils.loadFile
import java.io.File

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.test.assertTrue

@SpringBootTest(
    properties = [
        "spring.autoconfigure.exclude=" +
            "org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration," +
            "org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration"
    ]
)
class EventApplicationTests {

    @MockitoBean
    private lateinit var remoteSiteService: RemoteSiteService

    @Autowired
    private lateinit var deutscheOperBerlineventController: DeutscheOperBerlinEventController

    @Autowired
    private lateinit var staatsOperBerlinEventController: StaatsOperBerlinEventController

	@Test
	fun requestDeutscheOperBerlinEvent() {
        val response: ResponseEntity<DeutscheOperBerlinEventResponseDto> = deutscheOperBerlineventController
            .fetchEvents(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                "1"
            )
        assertTrue((response.body?.events?.size ?: 0) > 1)
	}

    @Test
    fun `parsing staatsoper berlin response verification`() {
        val date = LocalDate.of(LocalDate.now().year, LocalDate.now().month, 1)
        val responseMock = loadFile("mock/berlinStaatsOper.txt")

        whenever(
            remoteSiteService.fetch(
                any(),
                any(),
                any(),
                any<ParameterizedTypeReference<String>>()
            )
        ).thenReturn(responseMock)

        val response: ResponseEntity<StaatsOperBerlinEventResponseDto> = staatsOperBerlinEventController
            .fetchEvents(
                date
            )

        val body = response.body!!

        body.days.forEach { x->
            x.events.forEach { y ->
                File(getResource("dataBase.txt").file).appendText( y.toString() + "\n")}
        }

        val events = getElements("article", responseMock)
        assertThat(events.size).isEqualTo(getEvents(response))
    }

    @Test
    fun `transformation to event of staatsoper berlin response verification`() {
        val date = LocalDate.of(LocalDate.now().year, LocalDate.now().month, 1)
        val responseMock = loadFile("mock/berlinStaatsOper.txt")

        whenever(
            remoteSiteService.fetch(
                any(),
                any(),
                any(),
                any<ParameterizedTypeReference<String>>()
            )
        ).thenReturn(responseMock)

        val response: ResponseEntity<StaatsOperBerlinEventResponseDto> = staatsOperBerlinEventController
            .fetchEvents(
                date
            )

        val body = response.body!!
        body.toEvents().forEach {
                File(getResource("dataBase.txt").file).appendText( it.toString() + "\n")
        }

        val events = getElements("article", responseMock)
        assertThat(events.size).isEqualTo(getEvents(response))
    }

    private fun getEvents(response: ResponseEntity<StaatsOperBerlinEventResponseDto>): Int {
        return response.body
            ?.days
            ?.sumOf { it.events.size }
            ?: 0
    }
}
