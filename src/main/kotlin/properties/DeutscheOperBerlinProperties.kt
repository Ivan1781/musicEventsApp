package properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("event.deutsche-oper-berlin")
data class DeutscheOperBerlinProperties(
    val url: String,
    val params: Map<String, String> = emptyMap(),
    val headers: Map<String, String> = emptyMap(),
)
