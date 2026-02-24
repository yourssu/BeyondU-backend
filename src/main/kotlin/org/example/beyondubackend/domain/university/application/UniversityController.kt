package org.example.beyondubackend.domain.university.application

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.Parameters
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import org.example.beyondubackend.common.annotation.ExamScoreParams
import org.example.beyondubackend.common.dto.ApiResponse
import org.example.beyondubackend.domain.university.application.dto.UniversitySearchRequest
import org.example.beyondubackend.domain.university.business.UniversityService
import org.example.beyondubackend.domain.university.business.dto.UniversityDetailResponse
import org.example.beyondubackend.domain.university.business.dto.UniversityListResponse
import org.springdoc.core.annotations.ParameterObject
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
            - 필터링: nation, isExchange, isVisit, gpa, major, hasReview
            - 어학 점수 필터: TOEFL_IBT, TOEFL_ITP, IELTS, TOEIC, TOEIC_Speaking, HSK, JLPT, JPT, DELF, ZD
            - 페이지네이션: page (기본값 0), size (기본값 12)
            - 모든 파라미터는 optional이며, null이면 전체 조회
        """
    )
    @Parameters(value = [
        Parameter(
            name = "TOEFL_IBT", description = "TOEFL iBT 점수",
            required = false, `in` = ParameterIn.QUERY, schema = Schema(type = "number", example = "80")
        ),
        Parameter(
            name = "TOEFL_ITP", description = "TOEFL ITP 점수",
            required = false, `in` = ParameterIn.QUERY, schema = Schema(type = "number", example = "550")
        ),
        Parameter(
            name = "IELTS", description = "IELTS 점수",
            required = false, `in` = ParameterIn.QUERY, schema = Schema(type = "number", example = "6.5")
        ),
        Parameter(
            name = "TOEIC", description = "TOEIC 점수",
            required = false, `in` = ParameterIn.QUERY, schema = Schema(type = "number", example = "800")
        ),
        Parameter(
            name = "TOEIC_Speaking", description = "TOEIC Speaking 점수",
            required = false, `in` = ParameterIn.QUERY, schema = Schema(type = "number", example = "160")
        ),
        Parameter(
            name = "HSK", description = "HSK 급수",
            required = false, `in` = ParameterIn.QUERY, schema = Schema(type = "number", example = "4")
        ),
        Parameter(
            name = "JLPT", description = "JLPT 레벨 (1=N1 ~ 5=N5, 숫자가 낮을수록 높은 레벨)",
            required = false, `in` = ParameterIn.QUERY, schema = Schema(type = "number", example = "2")
        ),
        Parameter(
            name = "JPT", description = "JPT 점수",
            required = false, `in` = ParameterIn.QUERY, schema = Schema(type = "number", example = "700")
        ),
        Parameter(
            name = "DELF", description = "DELF 급수",
            required = false, `in` = ParameterIn.QUERY, schema = Schema(type = "number", example = "4")
        ),
        Parameter(
            name = "ZD", description = "ZD 급수 (독어 자격증)",
            required = false, `in` = ParameterIn.QUERY, schema = Schema(type = "number", example = "4")
        )
    ])
    @GetMapping
    fun getUniversities(
        @ParameterObject @ModelAttribute request: UniversitySearchRequest,
        @Parameter(hidden = true) @ExamScoreParams examScores: Map<String, Double>,
        @Parameter(description = "페이지 번호", example = "0")
        @RequestParam(defaultValue = "0") page: Int,
        @Parameter(description = "페이지 크기", example = "12")
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
