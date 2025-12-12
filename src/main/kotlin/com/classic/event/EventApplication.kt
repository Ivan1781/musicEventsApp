package com.classic.event

import properties.DeutscheOperBerlinProperties
import properties.StaatsOperBerlinProperties
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
