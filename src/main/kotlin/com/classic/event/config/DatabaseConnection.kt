package com.classic.event.config

import java.sql.Connection
import javax.sql.DataSource
import org.springframework.stereotype.Component

@Component
class DatabaseConnection(
    private val dataSource: DataSource
) {
    fun getConnection(): Connection = dataSource.connection

    fun <T> withConnection(action: (Connection) -> T): T =
        dataSource.connection.use { connection -> action(connection) }
}
