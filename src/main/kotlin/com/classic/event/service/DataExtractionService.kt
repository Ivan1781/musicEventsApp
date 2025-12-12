package com.classic.event.service

import com.classic.event.entity.EventEntity
import java.sql.ResultSet
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class DataExtractionService(
    private val jdbcTemplate: JdbcTemplate
) {

//    fun fetchEvents(limit: Int = 100): List<Event> {
//        val sql = """
//            select id, title, detail_url, date_time, duration, location, price, ticket_url, created_at, updated_at
//            from events
//            order by date_time nulls last
//            limit ?
//        """.trimIndent()
//
//        return jdbcTemplate.query(sql, arrayOf(limit)) { rs, _ -> rs.toEvent() }
//    }
//
//    fun queryForList(sql: String, vararg args: Any): List<Map<String, Any?>> =
//        jdbcTemplate.query(sql, args) { rs, _ ->
//            val meta = rs.metaData
//            (1..meta.columnCount).associate { idx ->
//                meta.getColumnLabel(idx) to rs.getObject(idx)
//            }
//        }

    private fun ResultSet.toEvent(): EventEntity =
        EventEntity(
            id = getObject("id", Long::class.java),
            title = getString("title"),
            detailUrl = getString("detail_url"),
            dateTime = getTimestamp("date_time")?.toLocalDateTime(),
            duration = getString("duration"),
            location = getString("location"),
            price = getString("price"),
            ticketUrl = getString("ticket_url"),
            createdAt = getTimestamp("created_at")?.toLocalDateTime(),
            updatedAt = getTimestamp("updated_at")?.toLocalDateTime()
        )
}
