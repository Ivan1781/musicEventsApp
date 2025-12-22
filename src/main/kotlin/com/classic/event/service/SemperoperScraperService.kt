package com.classic.event.service

import com.microsoft.playwright.*
import com.microsoft.playwright.options.WaitUntilState
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class SemperoperScraperService {
    fun loadEvents(): List<String> {
        logger.info("Semperoper scraper start")

        val config = JsScrapeConfig(
            startUrl = "https://www.semperoper.de/en/whats-on/calendar/calendar.html",
            fragmentSelector = ".frame-spielplan",
            waitForSelector = ".frame-spielplan",
            maxScrolls = 100,
            scrollDelayMs = 1200
        )

        try {
            Playwright.create().use { playwright ->
                val browser = playwright.chromium().launch(
                    BrowserType.LaunchOptions().setHeadless(true)
                )

                val page = browser.newPage()

                logger.debug("Navigating to {}", config.startUrl)
                page.navigate(
                    config.startUrl,
                    Page.NavigateOptions().setWaitUntil(WaitUntilState.NETWORKIDLE)
                )

                page.evaluate("window.scrollTo(0, document.body.scrollHeight)")
                page.waitForSelector(config.fragmentSelector)

//                var previousCount = 0
//                var stableIterations = 0

//                while (stableIterations < 3) {
//                    val currentCount = page.evalOnSelectorAll(
//                        config.fragmentSelector,
//                        "els => els.length"
//                    ) as Int
//
//                    if (currentCount == previousCount) {
//                        stableIterations++
//                    } else {
//                        stableIterations = 0
//                    }
//
//                    previousCount = currentCount
//
//                    page.evaluate(
//                        """
//                    const after = document.querySelector('.load-after-content');
//                    if (after) after.scrollIntoView({behavior: 'auto'});
//                    """
//                    )
//
//                    page.waitForTimeout(config.scrollDelayMs.toDouble())
//                }

                repeat(4) { iteration ->
                    logger.debug("Scroll iteration {}", iteration + 1)
                    page.evaluate(
                        """
                        const after = document.querySelector('.load-after-content');
                        if (after) after.scrollIntoView({behavior: 'auto'});
                        """
                    )

                    page.waitForTimeout(config.scrollDelayMs.toDouble())
                }

                val fragments = page.evalOnSelectorAll(
                    config.fragmentSelector,
                    "els => els.map(e => e.outerHTML)"
                )

                browser.close()

                @Suppress("UNCHECKED_CAST")
                return (fragments as List<String>).also {
                    logger.info("Semperoper scraper completed with {} fragments", it.size)
                }
            }
        } catch (ex: Exception) {
            logger.error("Semperoper scraper failed", ex)
            throw ex
        }
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(SemperoperScraperService::class.java)
    }
}
