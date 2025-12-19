package properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("event.semper-oper-dresden")
data class SemperOperDresdenProperties(
    val url: String,
    val params: Map<String, String> = emptyMap(),
    val headers: Map<String, String> = emptyMap()
)
