package org.example.beyondubackend.domain.languagerequirement.implement

interface LanguageRequirementRepository {
    fun findByUniversityId(universityId: Long): List<LanguageRequirement>
    fun findByUniversityIds(universityIds: List<Long>): Map<Long, List<LanguageRequirement>>
}
