package com.classic.event

import com.classic.event.controller.DeutscheOperBerlinEventController
import com.classic.event.controller.StaatsOperBerlinEventController
import com.classic.event.dto.DeutscheOperBerlinEventResponseDto
import com.classic.event.dto.StaatsOperBerlinEventDto
import com.classic.event.dto.StaatsOperBerlinEventResponseDto
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.ResponseEntity
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
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
    fun requestStaatsOperBerlinEvent() {
        val response: ResponseEntity<StaatsOperBerlinEventResponseDto> = staatsOperBerlinEventController
            .fetchEvents(
                LocalDate.of(LocalDate.now().year, LocalDate.now().month , 1)
            )
        println()

    }
}
