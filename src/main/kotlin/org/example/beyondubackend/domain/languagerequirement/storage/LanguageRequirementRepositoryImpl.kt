package org.example.beyondubackend.domain.languagerequirement.storage

import org.example.beyondubackend.domain.languagerequirement.implement.LanguageRequirement
import org.example.beyondubackend.domain.languagerequirement.implement.LanguageRequirementRepository
import org.springframework.stereotype.Repository

@Repository
class LanguageRequirementRepositoryImpl(
    private val languageRequirementJpaRepository: LanguageRequirementJpaRepository
) : LanguageRequirementRepository {

    override fun findByUniversityId(universityId: Long): List<LanguageRequirement> {
        return languageRequirementJpaRepository.findByUniversityIdAndIsAvailableTrue(universityId)
            .map { it.toDomain() }
    }

    override fun findByUniversityIds(universityIds: List<Long>): Map<Long, List<LanguageRequirement>> {
        if (universityIds.isEmpty()) return emptyMap()

        return languageRequirementJpaRepository.findByIsAvailableTrue()
            .filter { it.universityId in universityIds }
            .groupBy { it.universityId }
            .mapValues { entry -> entry.value.map { it.toDomain() } }
    }
}
