package com.classic.event.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "events")
data class EventEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val city: String? = null,

    val title: String? = null,

    @Column(name = "detail_url")
    val detailUrl: String? = null,

    @Column(name = "date_time")
    val dateTime: LocalDateTime? = null,

    val duration: String? = null,
    val location: String? = null,
    val price: String? = null,

    @Column(name = "ticket_url")
    val ticketUrl: String? = null,

    @Column(name = "created_at", insertable = false, updatable = false)
    val createdAt: LocalDateTime? = null,

    @Column(name = "updated_at", insertable = false, updatable = false)
    val updatedAt: LocalDateTime? = null
)
