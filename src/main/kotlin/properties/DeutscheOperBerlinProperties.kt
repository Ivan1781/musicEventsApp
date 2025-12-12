package properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("event.deutsche-oper-berlin")
data class DeutscheOperBerlinProperties(
    override val url: String,
    override val params: Map<String, String> = emptyMap(),
    override val headers: Map<String, String> = emptyMap(),
) : BaseProperties