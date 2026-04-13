package org.example.beyondubackend.domain.meta.application.dto

data class NationsByRegionResponse(
    val region: String,
    val nations: List<String>,
)