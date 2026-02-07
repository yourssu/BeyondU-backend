package org.example.beyondubackend.domain.university.business

import org.example.beyondubackend.common.dto.PageInfo
import org.example.beyondubackend.domain.university.business.dto.LanguageRequirementResponse
import org.example.beyondubackend.domain.university.business.dto.UniversityDetailResponse
import org.example.beyondubackend.domain.university.business.dto.UniversityListResponse
import org.example.beyondubackend.domain.university.business.query.UniversityQuery
import org.example.beyondubackend.domain.university.implement.UniversityReader
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UniversityServiceImpl(
    private val universityReader: UniversityReader
) : UniversityService {

    override fun getUniversities(query: UniversityQuery, pageable: Pageable): UniversityListResponse {
        val universityPage = universityReader.getUniversitiesWithFilters(
            nation = query.nation,
            isExchange = query.isExchange,
            isVisit = query.isVisit,
            search = query.search,
            pageable = pageable
        )

        val universities = universityPage.content.map { UniversityListResponse.UniversitySummaryDto.from(it) }

        val pageInfo = PageInfo(
            currentPage = universityPage.number,
            totalElements = universityPage.totalElements,
            totalPages = universityPage.totalPages,
            isLast = universityPage.isLast
        )

        return UniversityListResponse(universities, pageInfo)
    }

    override fun getUniversityDetail(id: Long): UniversityDetailResponse {
        val university = universityReader.getUniversityById(id)
        val languageRequirements = universityReader.getLanguageRequirementsByUniversityId(id)
            .map { LanguageRequirementResponse.from(it) }

        return UniversityDetailResponse.from(university, languageRequirements)
    }
}
