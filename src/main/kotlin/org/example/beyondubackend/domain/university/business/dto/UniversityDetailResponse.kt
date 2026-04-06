package org.example.beyondubackend.domain.university.business.dto

import org.example.beyondubackend.domain.university.implement.University
import java.util.Base64

data class UniversityDetailResponse(
    val id: Long,
    val nameKor: String,
    val nameEng: String,
    val nation: String,
    val region: String,
    val isExchange: Boolean,
    val isVisit: Boolean,
    val programType: String,
    val badge: String,
    val hasReview: Boolean,
    val reviewReportUrl: String?,
    val minGpa: Double,
    val websiteUrl: String?,
    val significantNote: String?,
    val remark: String?,
    val languageRequirements: List<LanguageRequirementResponse>,
    val availableMajors: List<String>?,
    val courseListUrl: String?,
    val studentCount: String?,
    val location: String?,
) {
    companion object {
        fun from(
            university: University,
            languageRequirements: List<LanguageRequirementResponse>
        ): UniversityDetailResponse {
            val programType = when {
                university.isExchange -> "일반교환"
                university.isVisit -> "방문교환"
                else -> ""
            }

            return UniversityDetailResponse(
                id = university.id!!,
                nameKor = university.nameKor,
                nameEng = university.nameEng,
                nation = university.nation,
                region = university.region,
                isExchange = university.isExchange,
                isVisit = university.isVisit,
                programType = programType,
                badge = university.badge,
                hasReview = university.hasReview,
                reviewReportUrl = if (university.hasReview) {
                    val encoded = Base64.getEncoder().encodeToString(university.nameEng.toByteArray(Charsets.UTF_8))
                    "https://study.ssu.ac.kr/community/exp_list.do?searchVal=$encoded&siteCd=01&boardCd=07"
                } else null,
                minGpa = university.minGpa,
                websiteUrl = university.websiteUrl,
                significantNote = university.significantNote,
                remark = university.remark,
                languageRequirements = languageRequirements,
                availableMajors = university.availableMajor
                    ?.split(",")
                    ?.map { it.trim() }
                    ?.filter { it.isNotBlank() }
                    ?.takeIf { it.isNotEmpty() },
                courseListUrl = university.availableSubject,
                studentCount = university.studentCount,
                location = university.location,
            )
        }
    }
}
