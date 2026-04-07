package org.example.beyondubackend.domain.university.business

import org.example.beyondubackend.common.dto.PageInfo
import org.example.beyondubackend.domain.languagerequirement.implement.LanguageRequirementReader
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
    private val universityReader: UniversityReader,
    private val languageRequirementReader: LanguageRequirementReader,
) : UniversityService {
    override fun getUniversities(
        query: UniversityQuery,
        pageable: Pageable,
    ): UniversityListResponse {
        val universityPage =
            universityReader.getUniversitiesWithFilters(
                nations = query.nations,
                regions = query.regions,
                languageGroups = query.languageGroups,
                isExchange = query.isExchange,
                isVisit = query.isVisit,
                search = query.search,
                gpa = query.gpa,
                major = query.major,
                hasReview = query.hasReview,
                examScores = query.examScores,
                pageable = pageable,
            )

        val universityIds = universityPage.content.mapNotNull { it.id }

        val languageRequirementsMap =
            if (universityIds.isNotEmpty()) {
                languageRequirementReader.findByUniversityIds(universityIds)
            } else {
                emptyMap()
            }

        val universities =
            universityPage.content.map { university ->
                val requirements = languageRequirementsMap[university.id] ?: emptyList()
                val summary = languageRequirementReader.generateSummary(requirements)
                UniversityListResponse.UniversitySummaryDto.from(university, summary)
            }

        val pageInfo =
            PageInfo(
                currentPage = universityPage.number,
                totalElements = universityPage.totalElements,
                totalPages = universityPage.totalPages,
                isLast = universityPage.isLast,
            )

        return UniversityListResponse(universities, pageInfo)
    }

    override fun getUniversityDetail(id: Long): UniversityDetailResponse {
        val university = universityReader.getUniversityById(id)
        val languageRequirements =
            languageRequirementReader
                .findByUniversityId(id)
                .map { LanguageRequirementResponse.from(it) }

        return UniversityDetailResponse.from(university, languageRequirements)
    }
}
