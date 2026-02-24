package org.example.beyondubackend.domain.university.implement

import org.example.beyondubackend.common.code.ErrorCode
import org.example.beyondubackend.common.exception.BusinessException
import org.example.beyondubackend.domain.languagerequirement.implement.LanguageRequirement
import org.example.beyondubackend.domain.languagerequirement.storage.LanguageRequirementJpaRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component

@Component
class UniversityReader(
    private val universityRepository: UniversityRepository,
    private val languageRequirementJpaRepository: LanguageRequirementJpaRepository
) {

    fun getUniversitiesWithFilters(
        nation: String?,
        isExchange: Boolean?,
        isVisit: Boolean?,
        search: String?,
        gpa: Double?,
        major: String?,
        hasReview: Boolean?,
        examScores: Map<String, Double>,
        pageable: Pageable
    ): Page<University> {
        return universityRepository.findAllWithFilters(
            nation, isExchange, isVisit, search, gpa, major, hasReview, examScores, pageable
        )
    }

    fun getUniversityById(id: Long): University {
        return universityRepository.findById(id)
            ?: throw BusinessException(ErrorCode.UNIVERSITY_NOT_FOUND)
    }

    fun getLanguageRequirementsByUniversityId(universityId: Long): List<LanguageRequirement> {
        return languageRequirementJpaRepository.findByUniversityIdAndIsAvailableTrue(universityId)
            .map { it.toDomain() }
    }
}
