package testUtils

import java.util.UUID

fun getTestUrl(baseUrl: String): String {
    return "${baseUrl}/${UUID.randomUUID()}"
}