package com.classic.event

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import properties.DeutscheOperBerlinProperties
import properties.HannoverStaatsTheaterProperties
import properties.SemperOperDresdenProperties
import properties.StaatsOperBerlinProperties

@SpringBootApplication
@EnableConfigurationProperties(
    value = [
        DeutscheOperBerlinProperties::class,
        HannoverStaatsTheaterProperties::class,
        StaatsOperBerlinProperties::class,
        SemperOperDresdenProperties::class
    ],
)
class EventApplication

fun main(args: Array<String>) {
    runApplication<EventApplication>(*args)
}
