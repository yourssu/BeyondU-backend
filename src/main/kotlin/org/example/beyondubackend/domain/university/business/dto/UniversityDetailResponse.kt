package org.example.beyondubackend.domain.university.business.dto

import org.example.beyondubackend.domain.university.implement.University

data class UniversityDetailResponse(
    val id: Long,
    val nameKor: String,
    val nameEng: String,
    val nation: String,
    val region: String,
    val thumbnailUrl: String?,
    val isExchange: Boolean,
    val isVisit: Boolean,
    val minGpa: Double,
    val availableSemester: String?,
    val websiteUrl: String?,
    val significantNote: String,
    val remark: String,
    val reviewSummary: String?,
    val rating: String?,
    val cost: Long?,
    val languageRequirements: List<LanguageRequirementResponse>,
) {
    companion object {
        fun from(
            university: University,
            languageRequirements: List<LanguageRequirementResponse>
        ): UniversityDetailResponse {
            return UniversityDetailResponse(
                id = university.id!!,
                nameKor = university.nameKor,
                nameEng = university.nameEng,
                nation = university.nation,
                region = university.region,
                thumbnailUrl = university.thumbnailUrl,
                isExchange = university.isExchange,
                isVisit = university.isVisit,
                minGpa = university.minGpa,
                availableSemester = university.availableSemester,
                websiteUrl = university.websiteUrl,
                significantNote = university.significantNote,
                remark = university.remark,
                reviewSummary = university.reviewSummary,
                rating = university.rating,
                cost = university.cost,
                languageRequirements = languageRequirements,
            )
        }
    }
}
