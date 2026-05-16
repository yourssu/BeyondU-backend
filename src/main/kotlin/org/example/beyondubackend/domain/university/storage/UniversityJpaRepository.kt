package org.example.beyondubackend.domain.university.storage

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UniversityJpaRepository : JpaRepository<UniversityEntity, Long> {
    @Query("SELECT DISTINCT u.region, u.nation FROM UniversityEntity u WHERE u.semester = :semester ORDER BY u.region, u.nation")
    fun findDistinctRegionAndNation(semester: String): List<Array<String>>
}
