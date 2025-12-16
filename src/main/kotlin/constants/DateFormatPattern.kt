package constants

import java.time.format.DateTimeFormatter

object DatePattern {
    val DATE_DOT_DMY = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val HOUR_MINUTE = DateTimeFormatter.ofPattern("HH:mm")
    val DATE_DASH_DMY = DateTimeFormatter.ofPattern("dd-MM-yyyy")
}