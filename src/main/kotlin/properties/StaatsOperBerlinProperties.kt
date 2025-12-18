package properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("event.staatsoper-berlin")
data class StaatsOperBerlinProperties(
    val url: String,
    val params: Map<String, String> = emptyMap(),
    val headers: Map<String, String> = emptyMap(),
)
