package com.classic.event.dto

interface EventOverviewBase {
    val title: String?
    val contentCategory: String?
    val duration: Int?
    val city: String?
    val location: String?
    val status: String?
    val priceUrl: String?
    val priceValueMinMax: String?
    val ageIndication: String?
    val opusInfo: String?
}
