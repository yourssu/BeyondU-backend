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
        val region: String,
        val thumbnailUrl: String?,
        val isExchange: Boolean,
        val isVisit: Boolean,
        val availableSemester: String?,
        val reviewSummary: String?,
        val rating: String?
    ) {
        companion object {
            fun from(university: University): UniversitySummaryDto {
                return UniversitySummaryDto(
                    id = university.id!!,
                    nameKor = university.nameKor,
                    nameEng = university.nameEng,
                    nation = university.nation,
                    region = university.region,
                    thumbnailUrl = university.thumbnailUrl,
                    isExchange = university.isExchange,
                    isVisit = university.isVisit,
                    availableSemester = university.availableSemester,
                    reviewSummary = university.reviewSummary,
                    rating = university.rating
                )
            }
        }
    }
}
