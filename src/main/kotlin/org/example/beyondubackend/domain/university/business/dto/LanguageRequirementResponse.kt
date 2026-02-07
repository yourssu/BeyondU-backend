package org.example.beyondubackend.domain.university.business.dto

import org.example.beyondubackend.domain.languagerequirement.implement.LanguageRequirement

data class LanguageRequirementResponse(
    val languageGroup: String,
    val examType: String,
    val minScore: Double
) {
    companion object {
        fun from(languageRequirement: LanguageRequirement): LanguageRequirementResponse {
            return LanguageRequirementResponse(
                languageGroup = languageRequirement.languageGroup,
                examType = languageRequirement.examType,
                minScore = languageRequirement.minScore
            )
        }
    }
}
