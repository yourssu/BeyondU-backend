package org.example.beyondubackend.domain.meta.application.dto

data class SubMajorResponse(
    val enumName: String,
    val name: String,
    val koreanMajors: List<String>,
)
