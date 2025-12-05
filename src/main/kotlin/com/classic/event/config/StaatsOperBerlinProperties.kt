package com.classic.event.config

import com.classic.event.dto.BaseProperties
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("event.staatsoper")
data class StaatsOperBerlinProperties(
    override val url: String,
    override val params: Map<String, String> = emptyMap(),
    override val headers: Map<String, String> = emptyMap(),
) : BaseProperties
