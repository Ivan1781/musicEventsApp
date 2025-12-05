package com.classic.event.config

import java.io.ByteArrayInputStream
import java.io.InputStream
import java.nio.charset.StandardCharsets
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatusCode
import org.springframework.http.client.ClientHttpResponse
import org.springframework.util.StreamUtils
import org.springframework.web.client.RestClient
import org.springframework.web.filter.CommonsRequestLoggingFilter

@Configuration
class LoggingConfig {
    @Bean
    fun restClientBuilder(): RestClient.Builder =
        RestClient.builder()
            .requestInterceptor { request, body, execution ->
                val start = System.nanoTime()
                logger.info("HTTP outbound -> {} {} headers={} body {}", request.method, request.uri, request.headers, body)

                val response = execution.execute(request, body)
                val bufferedResponse = LoggingClientHttpResponse(response)
                val durationMs = (System.nanoTime() - start) / 1_000_000

                if (logger.isDebugEnabled) {
                    val responseBody = bufferedResponse.body.use {
                        StreamUtils.copyToString(it, StandardCharsets.UTF_8)
                    }
                    logger.debug(
                        "HTTP outbound <- {} {} status={} durationMs={} body={}",
                        request.method,
                        request.uri,
                        bufferedResponse.statusCode,
                        durationMs,
                        responseBody,
                    )
                } else {
                    logger.info(
                        "HTTP outbound <- {} {} status={} durationMs={} body {}",
                        request.method,
                        request.uri,
                        bufferedResponse.statusCode,
                        String(bufferedResponse.body.readAllBytes()),
                        durationMs,
                    )
                }

                bufferedResponse
            }

    @Bean
    @ConditionalOnMissingBean
    fun requestLoggingFilter(): CommonsRequestLoggingFilter {
        val filter = CommonsRequestLoggingFilter()
        filter.setIncludeQueryString(true)
        filter.setIncludePayload(true)
        filter.setMaxPayloadLength(1024)
        filter.setIncludeHeaders(false)
        filter.setAfterMessagePrefix("HTTP inbound <-")
        filter.setAfterMessageSuffix("")
        return filter
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(LoggingConfig::class.java)
    }

    private class LoggingClientHttpResponse(
        private val delegate: ClientHttpResponse,
    ) : ClientHttpResponse {
        private val bodyBytes: ByteArray by lazy { delegate.body.use(InputStream::readAllBytes) }

        override fun getStatusCode(): HttpStatusCode = delegate.statusCode
        override fun getStatusText(): String = delegate.statusText

        override fun getHeaders(): HttpHeaders = delegate.headers

        override fun getBody(): InputStream = ByteArrayInputStream(bodyBytes)

        override fun close() = delegate.close()
    }
}
