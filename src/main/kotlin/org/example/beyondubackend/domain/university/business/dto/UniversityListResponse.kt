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
        val badge: String,
        val isExchange: Boolean,
        val isVisit: Boolean,
        val programType: String,
        val languageRequirementSummary: String?,
        val reviewStatus: String
    ) {
        companion object {
            fun from(university: University, languageRequirementSummary: String?): UniversitySummaryDto {
                val reviewStatus = when {
                    university.hasReview && university.reviewYear != null ->
                        "후기 있음 (${university.reviewYear})"
                    university.hasReview ->
                        "후기 있음"
                    else ->
                        "후기 없음"
                }

                val programType = when {
                    university.isExchange -> "일반교환"
                    university.isVisit -> "방문교환"
                    else -> ""
                }

                return UniversitySummaryDto(
                    id = university.id!!,
                    nameKor = university.nameKor,
                    nameEng = university.nameEng,
                    nation = university.nation,
                    badge = university.badge,
                    isExchange = university.isExchange,
                    isVisit = university.isVisit,
                    programType = programType,
                    languageRequirementSummary = languageRequirementSummary,
                    reviewStatus = reviewStatus
                )
            }
        }
    }
}
