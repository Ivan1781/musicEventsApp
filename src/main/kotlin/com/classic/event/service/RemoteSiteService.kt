package com.classic.event.service

import java.net.URI
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Service
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestClient
import org.springframework.web.client.RestClientResponseException
import org.springframework.web.util.UriComponentsBuilder

@Service
class RemoteSiteService(
    restClientBuilder: RestClient.Builder,
) {
    private val client: RestClient = restClientBuilder.build()

    fun <T : Any> fetch(
        url: String,
        headers: Map<String, String> = emptyMap(),
        queryParams: Map<String, String> = emptyMap(),
        responseType: ParameterizedTypeReference<T>,
    ): T {
        val targetUri = buildUri(url, queryParams)
        return try {
            client
                .get()
                .uri(targetUri)
                .headers { httpHeaders ->
                    headers.forEach { (name, value) -> httpHeaders.set(name, value) }
                }
                .retrieve()
                .body(responseType)
                ?: throw IllegalStateException("Empty response from $targetUri")
        } catch (ex: RestClientResponseException) {
            throw IllegalStateException(
                "Remote call failed with status ${ex.statusCode}: ${ex.responseBodyAsString}",
                ex,
            )
        } catch (ex: ResourceAccessException) {
            throw IllegalStateException("Remote call could not reach $targetUri", ex)
        }
    }

    private fun buildUri(url: String, queryParams: Map<String, String>): URI {
        val builder = UriComponentsBuilder.fromUriString(url)
        queryParams.forEach { (name, value) -> builder.queryParam(name, value) }
        return builder.build(true).toUri()
    }
}
