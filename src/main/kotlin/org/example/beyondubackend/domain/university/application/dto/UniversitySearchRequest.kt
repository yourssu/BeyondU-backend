package org.example.beyondubackend.domain.university.application.dto

import io.swagger.v3.oas.annotations.media.Schema
import org.example.beyondubackend.common.code.ErrorCode
import org.example.beyondubackend.common.enums.LanguageGroup
import org.example.beyondubackend.common.enums.Nation
import org.example.beyondubackend.common.enums.Region
import org.example.beyondubackend.common.exception.BusinessException
import org.example.beyondubackend.domain.university.business.query.UniversityQuery

data class  UniversitySearchRequest(
    @Schema(description = "국가 필터 단일값 (하위 호환, 예: ?nation=USA)")
    val nation: String? = null,
    @Schema(description = "국가 필터 복수값 (예: ?nations=USA&nations=JPN)", example = "USA")
    val nations: List<String>? = null,
    @Schema(description = "대륙 필터 복수값 (예: ?regions=아시아&regions=유럽)")
    val regions: List<String>? = null,
    @Schema(description = "언어권 필터 복수값 (예: ?languageGroups=ENGLISH&languageGroups=JAPANESE)", example = "ENGLISH")
    val languageGroups: List<String>? = null,
    @Schema(description = "교환학생 가능 여부")
    val isExchange: Boolean? = null,
    @Schema(description = "방문학생 가능 여부")
    val isVisit: Boolean? = null,
    // TO DO: 대학 이름 검색 사용시 추가
    @Schema(hidden = true)
    val search: String? = null,
    @Schema(description = "최소 GPA (입력한 GPA 이상 지원 가능한 학교 조회)", example = "3.5")
    val gpa: Double? = null,
    @Schema(description = "전공 필터")
    val major: String? = null,
    @Schema(description = "후기 보유 여부")
    val hasReview: Boolean? = null,
) {
    fun toQuery(examScores: Map<String, Double>): UniversityQuery {
        val mergedNations =
            (nations.orEmpty() + listOfNotNull(nation))
                .map { it.trim() }
                .filter { it.isNotBlank() }
                .distinct()
                .takeIf { it.isNotEmpty() }

        validateNations(mergedNations)
        validateRegions(regions)
        validateLanguageGroups(languageGroups)

        return UniversityQuery(
            nations = mergedNations,
            regions = regions,
            languageGroups = languageGroups,
            isExchange = isExchange,
            isVisit = isVisit,
            search = search,
            gpa = gpa,
            major = major,
            hasReview = hasReview,
            examScores = examScores,
        )
    }

    private fun validateNations(nations: List<String>?) {
        if (nations.isNullOrEmpty()) return
        val validNames = Nation.entries.map { it.displayName }.toSet()
        val invalid = nations.filter { it !in validNames }
        if (invalid.isNotEmpty()) {
            throw BusinessException(ErrorCode.INVALID_INPUT, "유효하지 않은 국가: ${invalid.joinToString()}")
        }
    }

    private fun validateRegions(regions: List<String>?) {
        if (regions.isNullOrEmpty()) return
        val validNames = Region.entries.map { it.displayName }.toSet()
        val invalid = regions.filter { it !in validNames }
        if (invalid.isNotEmpty()) {
            throw BusinessException(ErrorCode.INVALID_INPUT, "유효하지 않은 대륙: ${invalid.joinToString()}")
        }
    }

    private fun validateLanguageGroups(languageGroups: List<String>?) {
        if (languageGroups.isNullOrEmpty()) return
        val validNames = LanguageGroup.entries.map { it.name }.toSet()
        val invalid = languageGroups.filter { it !in validNames }
        if (invalid.isNotEmpty()) {
            throw BusinessException(ErrorCode.INVALID_INPUT, "유효하지 않은 언어권: ${invalid.joinToString()}")
        }
    }
}
