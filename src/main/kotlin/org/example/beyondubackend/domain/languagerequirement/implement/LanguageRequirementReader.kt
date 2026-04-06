package org.example.beyondubackend.domain.languagerequirement.implement

import org.example.beyondubackend.common.enums.ExamType
import org.springframework.stereotype.Component

@Component
class LanguageRequirementReader(
    private val languageRequirementRepository: LanguageRequirementRepository,
) {
    fun findByUniversityId(universityId: Long): List<LanguageRequirement> = languageRequirementRepository.findByUniversityId(universityId)

    fun findByUniversityIds(universityIds: List<Long>): Map<Long, List<LanguageRequirement>> =
        languageRequirementRepository.findByUniversityIds(universityIds)

    fun generateSummary(requirements: List<LanguageRequirement>): String? {
        if (requirements.isEmpty()) return null

        return requirements.joinToString(" / ") { requirement ->
            val examType = ExamType.fromDisplayName(requirement.examType)
            val scoreText =
                if (examType != null) {
                    "${examType.prefix}${formatScore(requirement.minScore)}${examType.suffix}"
                } else {
                    formatScore(requirement.minScore)
                }
            "${requirement.examType} $scoreText"
        }
    }

    private fun formatScore(score: Double): String =
        if (score % 1.0 == 0.0) {
            score.toInt().toString()
        } else {
            score.toString()
        }
}
