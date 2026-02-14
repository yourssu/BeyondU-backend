package org.example.beyondubackend.domain.languagerequirement.storage

import org.springframework.data.jpa.repository.JpaRepository

interface LanguageRequirementJpaRepository : JpaRepository<LanguageRequirementEntity, Long> {
    fun findByUniversityIdAndIsAvailableTrue(universityId: Long): List<LanguageRequirementEntity>
    fun findByIsAvailableTrue(): List<LanguageRequirementEntity>
}
