package org.example.beyondubackend.domain.university.business.query

data class UniversityQuery(
    val nation: String? = null,
    val isExchange: Boolean? = null,
    val isVisit: Boolean? = null,
    val search: String? = null,
    val gpa: Double? = null,
    val nations: String? = null,
    val major: String? = null,
    val hasReview: Boolean? = null,
    val examScores: Map<String, Double> = emptyMap()
)
