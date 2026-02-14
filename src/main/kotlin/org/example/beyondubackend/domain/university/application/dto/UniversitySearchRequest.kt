package org.example.beyondubackend.domain.university.application.dto

import org.example.beyondubackend.domain.university.business.query.UniversityQuery

data class UniversitySearchRequest(
    val nation: String? = null,
    val isExchange: Boolean? = null,
    val isVisit: Boolean? = null,
    val search: String? = null,
    val gpa: Double? = null,
    val nations: String? = null,
    val major: String? = null,
    val hasReview: Boolean? = null
) {
    fun toQuery(examScores: Map<String, Double>): UniversityQuery {
        return UniversityQuery(
            nation = nation,
            isExchange = isExchange,
            isVisit = isVisit,
            search = search,
            gpa = gpa,
            nations = nations,
            major = major,
            hasReview = hasReview,
            examScores = examScores
        )
    }
}
