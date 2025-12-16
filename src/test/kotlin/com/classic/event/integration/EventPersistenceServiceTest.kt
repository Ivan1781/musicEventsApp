package com.classic.event.integration

import com.classic.event.entity.EventEntity
import com.classic.event.repository.EventRepository
import com.classic.event.service.EventPersistenceService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.LocalDateTime
import java.util.UUID

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EventPersistenceServiceTest {

    companion object {
        @Container
        val postgres: PostgreSQLContainer<*> = PostgreSQLContainer("postgres:16-alpine").apply {
            withDatabaseName("eventdbtest")
            withUsername("eventusertest")
            withPassword("passtest")
        }

        @JvmStatic
        @DynamicPropertySource
        fun registerDataSourceProperties(registry: DynamicPropertyRegistry) {
            postgres.start()
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
        }
    }

    @Autowired
    private lateinit var service: EventPersistenceService

    @Autowired
    private lateinit var repository: EventRepository

    @Test
    fun `save event with all fields`() {
        val entity = EventEntity(
            title = "Test event",
            city = "Norilsk",
            detailUrl = "https://example.com/events/${UUID.randomUUID()}",
            dateTime = LocalDateTime.of(2024, 1, 1, 12, 0),
            duration = "120m",
            location = "Test venue",
            price = "20 EUR",
            ticketUrl = "https://example.com/tickets/${UUID.randomUUID()}"
        )

        val saved = service.save(entity)
        val reloaded = repository.findById(saved.id!!).orElse(null)

        assertThat(reloaded).isNotNull
        assertThat(reloaded?.city).isEqualTo(entity.city)
        assertThat(reloaded?.detailUrl).isEqualTo(entity.detailUrl)
        assertThat(reloaded?.dateTime).isEqualTo(entity.dateTime)
        assertThat(reloaded?.ticketUrl).isEqualTo(entity.ticketUrl)
    }
}
