package org.example.beyondubackend.domain.university.application.dto

import io.swagger.v3.oas.annotations.media.Schema
import org.example.beyondubackend.domain.university.business.query.UniversityQuery

data class UniversitySearchRequest(
    @Schema(description = "국가 필터 (예: 미국, 일본)")
    val nation: String? = null,
    @Schema(description = "교환학생 가능 여부")
    val isExchange: Boolean? = null,
    @Schema(description = "방문학생 가능 여부")
    val isVisit: Boolean? = null,
    //TO DO: 대학 이름 검색 사용시 추가
    @Schema(hidden = true)
    val search: String? = null,
    @Schema(description = "최소 GPA (입력한 GPA 이상 지원 가능한 학교 조회)", example = "3.5")
    val gpa: Double? = null,
    @Schema(description = "전공 필터")
    val major: String? = null,
    @Schema(description = "후기 보유 여부")
    val hasReview: Boolean? = null
) {
    fun toQuery(examScores: Map<String, Double>): UniversityQuery {
        return UniversityQuery(
            nation = nation,
            isExchange = isExchange,
            isVisit = isVisit,
            search = search,
            gpa = gpa,
            major = major,
            hasReview = hasReview,
            examScores = examScores
        )
    }
}
