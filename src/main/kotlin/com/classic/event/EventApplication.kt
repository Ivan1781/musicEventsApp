package com.classic.event

import com.classic.event.config.DeutscheOperBerlinProperties
import com.classic.event.config.StaatsOperBerlinProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(
    value = [DeutscheOperBerlinProperties::class, StaatsOperBerlinProperties::class],
)
class EventApplication

fun main(args: Array<String>) {
    runApplication<EventApplication>(*args)
}
