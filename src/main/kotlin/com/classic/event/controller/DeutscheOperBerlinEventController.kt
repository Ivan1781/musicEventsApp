package com.classic.event.controller

import com.classic.event.config.DeutscheOperBerlinProperties
import com.classic.event.dto.DeutscheOperBerlinEventResponseDto
import com.classic.event.service.RemoteSiteService
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class DeutscheOperBerlinEventController(
    remoteSiteService: RemoteSiteService,
    properties: DeutscheOperBerlinProperties,
) : BaseController<DeutscheOperBerlinProperties>(remoteSiteService, properties) {
    fun fetchEvents(
        @RequestParam("date") date: String,
        @RequestParam(name = "p", defaultValue = "1") page: String,
    ): ResponseEntity<DeutscheOperBerlinEventResponseDto> {
        val body =
            fetch(
                queryParams = mapOf("date" to date, "p" to page),
                responseType = object : ParameterizedTypeReference<DeutscheOperBerlinEventResponseDto>() {},
            )
        return ResponseEntity.ok(body)
    }
}
