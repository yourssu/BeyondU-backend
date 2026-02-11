package org.example.beyondubackend.domain.university.business

import org.example.beyondubackend.domain.university.business.dto.UniversityDetailResponse
import org.example.beyondubackend.domain.university.business.dto.UniversityListResponse
import org.example.beyondubackend.domain.university.business.query.UniversityQuery
import org.springframework.data.domain.Pageable

interface UniversityService {

    fun getUniversities(query: UniversityQuery, pageable: Pageable): UniversityListResponse

    fun getUniversityDetail(id: Long): UniversityDetailResponse
}
