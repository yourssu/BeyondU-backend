package org.example.beyondubackend.domain.university.application

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.example.beyondubackend.common.annotation.ExamScoreParams
import org.example.beyondubackend.common.dto.ApiResponse
import org.example.beyondubackend.domain.university.application.dto.UniversitySearchRequest
import org.example.beyondubackend.domain.university.business.UniversityService
import org.example.beyondubackend.domain.university.business.dto.UniversityDetailResponse
import org.example.beyondubackend.domain.university.business.dto.UniversityListResponse
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "University", description = "대학교 조회 API")
@RestController
@RequestMapping("/api/v1/universities")
class UniversityController(
    private val universityService: UniversityService
) {

    @Operation(
        summary = "대학교 목록 조회",
        description = """
            대학교 목록을 조회합니다.
            - 필터링: nation, isExchange, isVisit, gpa, nations, major, hasReview
            - 언어 점수: TOEFL, IELTS, TOEIC, JLPT 등
            - 페이지네이션: page
        """
    )
    @GetMapping
    fun getUniversities(
        @ModelAttribute request: UniversitySearchRequest,
        @Parameter(description = "언어 시험 점수 (예: TOEFL=80, IELTS=6.5)", required = false)
        @ExamScoreParams examScores: Map<String, Double>,
        @Parameter(description = "페이지 번호", example = "0")
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "12") size: Int
    ): ResponseEntity<ApiResponse<UniversityListResponse>> {
        val pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("nameEng"), Sort.Order.asc("nameKor")))
        val query = request.toQuery(examScores)
        val result = universityService.getUniversities(query, pageable)
        return ApiResponse.success(result)
    }

    @Operation(
        summary = "대학교 상세 조회",
        description = "대학교 ID로 상세 정보를 조회합니다. 언어 요구사항 정보도 함께 반환됩니다."
    )
    @GetMapping("/{id}")
    fun getUniversityDetail(
        @Parameter(description = "대학교 ID", example = "1", required = true)
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse<UniversityDetailResponse>> {
        val result = universityService.getUniversityDetail(id)
        return ApiResponse.success(result)
    }
}
