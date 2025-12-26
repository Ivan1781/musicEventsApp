package properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("event.hannover-staatstheater")
data class HannoverStaatsTheaterProperties(
    val url: String,
    val params: Map<String, String> = emptyMap(),
    val headers: Map<String, String> = emptyMap()
)
