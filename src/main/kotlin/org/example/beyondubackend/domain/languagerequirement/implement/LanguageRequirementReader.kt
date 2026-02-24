package org.example.beyondubackend.domain.languagerequirement.implement

import org.springframework.stereotype.Component

@Component
class LanguageRequirementReader(
    private val languageRequirementRepository: LanguageRequirementRepository
) {

    fun findByUniversityId(universityId: Long): List<LanguageRequirement> {
        return languageRequirementRepository.findByUniversityId(universityId)
    }

    fun findByUniversityIds(universityIds: List<Long>): Map<Long, List<LanguageRequirement>> {
        return languageRequirementRepository.findByUniversityIds(universityIds)
    }

    fun generateSummary(requirements: List<LanguageRequirement>): String? {
        if (requirements.isEmpty()) return null

        return requirements.joinToString(" / ") { requirement ->
            when {
                requirement.examType.equals(ExamType.HSK.displayName, ignoreCase = true) && requirement.levelCode != null -> {
                    "${ExamType.HSK.displayName} ${requirement.levelCode}"
                }
                requirement.examType.equals(ExamType.HSK.displayName, ignoreCase = true) -> {
                    "${ExamType.HSK.displayName} ${requirement.minScore.toInt()}"
                }
                else -> {
                    "${requirement.examType} ${formatScore(requirement.minScore)}"
                }
            }
        }
    }

    private fun formatScore(score: Double): String {
        return if (score % 1.0 == 0.0) {
            score.toInt().toString()
        } else {
            score.toString()
        }
    }
}
