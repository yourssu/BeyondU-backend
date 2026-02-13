package org.example.beyondubackend.domain.university.implement

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface UniversityRepository {
    fun findAllWithFilters(
        nation: String?,
        isExchange: Boolean?,
        isVisit: Boolean?,
        search: String?,
        gpa: Double?,
        nations: String?,
        major: String?,
        hasReview: Boolean?,
        examScores: Map<String, Double>,
        pageable: Pageable
    ): Page<University>

    fun findById(id: Long): University?
}
