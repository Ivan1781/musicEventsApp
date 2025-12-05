package com.classic.event.dto

interface BaseProperties {
    val url: String
    val params: Map<String, String>
    val headers: Map<String, String>
        get() = emptyMap()
}
