package org.example.beyondubackend.domain.meta.application.dto

data class MajorCategoryResponse(
    val category: String,
    val majors: List<SubMajorResponse>,
)
