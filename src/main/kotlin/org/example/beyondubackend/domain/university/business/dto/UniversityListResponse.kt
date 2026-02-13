package org.example.beyondubackend.domain.university.business.dto

import org.example.beyondubackend.common.dto.PageInfo
import org.example.beyondubackend.domain.university.implement.University

data class UniversityListResponse(
    val universities: List<UniversitySummaryDto>,
    val pageInfo: PageInfo
) {
    data class UniversitySummaryDto(
        val id: Long,
        val nameKor: String,
        val nameEng: String,
        val nation: String,
        val badge: String?,
        val isExchange: Boolean,
        val isVisit: Boolean,
        val languageRequirementSummary: String?,
        val hasReview: Boolean
    ) {
        val programType: String
            get() = when {
                isExchange -> "일반교환"
                isVisit -> "방문교환"
                else -> ""
            }
        companion object {
            fun from(university: University, languageRequirementSummary: String?): UniversitySummaryDto {
                return UniversitySummaryDto(
                    id = university.id!!,
                    nameKor = university.nameKor,
                    nameEng = university.nameEng,
                    nation = university.nation,
                    badge = university.badge,
                    isExchange = university.isExchange,
                    isVisit = university.isVisit,
                    languageRequirementSummary = languageRequirementSummary,
                    hasReview = university.hasReview
                )
            }
        }
    }
}
