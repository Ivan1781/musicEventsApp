package com.classic.event.service

data class JsScrapeConfig(
    val startUrl: String,

    /** CSS selector for fragments to extract */
    val fragmentSelector: String,

    /** Max number of scroll attempts (safety limit) */
    val maxScrolls: Int = 2,

    /** Delay after each scroll (ms) */
    val scrollDelayMs: Long = 1500,

    /** Optional selector to wait for before scraping starts */
    val waitForSelector: String? = null
)
