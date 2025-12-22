package com.classic.event.unit

import com.classic.event.service.SemperoperScraperService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SemperScraperServiceTests {

    @Autowired
    private lateinit var semperScraperService: SemperoperScraperService

    @Test
    fun `parse events from mock file`() {
        val ev = semperScraperService.loadEvents()
    }
}
