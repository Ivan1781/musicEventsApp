package com.classic.event.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.security.MessageDigest
import java.time.LocalDateTime

@Entity
@Table(name = "events")
data class EventEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val title: String? = null,

    val city: String? = null,

    @Column(name = "detail_url", unique = true)
    val detailUrl: String? = null,

    @Column(name = "date_time")
    val dateTime: LocalDateTime? = null,

    val duration: String? = null,
    val location: String? = null,
    val price: String? = null,

    @Column(name = "ticket_url")
    val ticketUrl: String? = null,

    val category: String? = null,
    val author: String? = null,

    @Column(name = "created_at", insertable = false, updatable = false)
    val createdAt: LocalDateTime? = null,

    @Column(name = "updated_at", insertable = false, updatable = false)
    val updatedAt: LocalDateTime? = null
) {
    fun calculateHash(): String {
        val dataToHash = listOfNotNull(
            title,
            city,
            detailUrl,
            dateTime?.toString(),
            duration,
            location,
            price,
            ticketUrl,
            category,
            author
        ).joinToString("|")

        return MessageDigest.getInstance("SHA-256")
            .digest(dataToHash.toByteArray())
            .joinToString("") { "%02x".format(it) }
    }
}
