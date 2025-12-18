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
import testUtils.getTestUrl
import java.time.LocalDateTime

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

        val baseEntity = EventEntity(
            title = "Test event",
            city = "Norilsk",
            detailUrl = getTestUrl("https://example.com/events"),
            dateTime = LocalDateTime.of(2024, 1, 1, 12, 0),
            duration = "120m",
            location = "Test venue",
            price = "20 EUR",
            ticketUrl = getTestUrl("https://example.com/tickets")
        )
    }

    @Autowired
    private lateinit var service: EventPersistenceService

    @Autowired
    private lateinit var repository: EventRepository

    @Test
    fun `save event with all fields`() {
        val saved = service.save(baseEntity)
        val persisted = repository.findById(saved.id!!).orElse(null)

        assertThat(persisted).isNotNull
        assertThat(persisted?.city).isEqualTo(baseEntity.city)
        assertThat(persisted?.detailUrl).isEqualTo(baseEntity.detailUrl)
        assertThat(persisted?.dateTime).isEqualTo(baseEntity.dateTime)
        assertThat(persisted?.ticketUrl).isEqualTo(baseEntity.ticketUrl)
    }

    @Test
    fun `saveAll with duplicated events`() {
        val entity = baseEntity.copy(detailUrl = getTestUrl("https://example.com/berlinevents") )
        val entityList = listOf(entity, entity.copy())

        val saved = service.saveAll(entityList)

        assertThat(saved.size).isEqualTo(1)

        val savedEntity = saved.first()
        val persisted = repository.findById(savedEntity.id!!)

        assertThat(persisted.get().id).isEqualTo(savedEntity.id)
        assertThat(persisted.get().detailUrl).isEqualTo(savedEntity.detailUrl)
    }
}
