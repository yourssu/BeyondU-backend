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
    private val languageRequirementReader: LanguageRequirementReader
) : UniversityService {

    override fun getUniversities(query: UniversityQuery, pageable: Pageable): UniversityListResponse {
        val universityPage = universityReader.getUniversitiesWithFilters(
            nation = query.nation,
            isExchange = query.isExchange,
            isVisit = query.isVisit,
            search = query.search,
            gpa = query.gpa,
            nations = query.nations,
            major = query.major,
            hasReview = query.hasReview,
            examScores = query.examScores,
            pageable = pageable
        )

        // 대학 ID 목록 추출
        val universityIds = universityPage.content.map { it.id!! }

        // 언어 요구사항 일괄 조회
        val languageRequirementsMap = if (universityIds.isNotEmpty()) {
            languageRequirementReader.findByUniversityIds(universityIds)
        } else {
            emptyMap()
        }

        // DTO 변환 (languageRequirementSummary 포함)
        val universities = universityPage.content.map { university ->
            val requirements = languageRequirementsMap[university.id] ?: emptyList()
            val summary = languageRequirementReader.generateSummary(requirements)
            UniversityListResponse.UniversitySummaryDto.from(university, summary)
        }

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
