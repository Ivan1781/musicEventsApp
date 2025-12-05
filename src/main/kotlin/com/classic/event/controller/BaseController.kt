package com.classic.event.controller

import com.classic.event.dto.BaseProperties
import com.classic.event.service.RemoteSiteService
import org.springframework.core.ParameterizedTypeReference

abstract class BaseController<T : BaseProperties>(
    protected val remoteSiteService: RemoteSiteService,
    protected val properties: T,
) {
    protected fun <R : Any> fetch(
        queryParams: Map<String, String>,
        responseType: ParameterizedTypeReference<R>,
        headers: Map<String, String> = emptyMap(),
    ): R =
        remoteSiteService.fetch(
            url = properties.url,
            headers = properties.headers + headers,
            queryParams = properties.params + queryParams,
            responseType = responseType,
        )
}
