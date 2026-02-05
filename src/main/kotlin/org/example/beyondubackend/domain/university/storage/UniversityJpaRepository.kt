package org.example.beyondubackend.domain.university.storage

import org.springframework.data.jpa.repository.JpaRepository

interface UniversityJpaRepository : JpaRepository<UniversityEntity, Long>
