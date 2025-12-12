package com.classic.event.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DataSourceConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.hikari")
    fun hikariConfig(): HikariConfig = HikariConfig()

    @Bean
    fun dataSource(
        properties: DataSourceProperties,
        hikariConfig: HikariConfig
    ): DataSource {
        val hikariDataSource = properties
            .initializeDataSourceBuilder()
            .type(HikariDataSource::class.java)
            .build()

        hikariDataSource.poolName = "event-hikari"
        hikariDataSource.maximumPoolSize = hikariConfig.maximumPoolSize
        hikariDataSource.minimumIdle = hikariConfig.minimumIdle
        hikariDataSource.connectionTimeout = hikariConfig.connectionTimeout
        hikariDataSource.idleTimeout = hikariConfig.idleTimeout
        hikariDataSource.maxLifetime = hikariConfig.maxLifetime

        return hikariDataSource
    }
}
