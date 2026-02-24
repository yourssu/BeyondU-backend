package org.example.beyondubackend.domain.meta.application

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.example.beyondubackend.common.dto.ApiResponse
import org.example.beyondubackend.common.enums.ExamType
import org.example.beyondubackend.common.enums.Nation
import org.example.beyondubackend.domain.meta.application.dto.ExamTypeResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Meta", description = "공통 코드 조회 API")
@RestController
@RequestMapping("/api/v1/meta")
class MetaController {

    @Operation(summary = "국가 목록 조회", description = "university 필터링에 사용 가능한 국가 목록을 반환합니다.")
    @GetMapping("/nations")
    fun getNations(): ResponseEntity<ApiResponse<List<String>>> {
        val nations = Nation.entries.map { it.displayName }
        return ApiResponse.success(nations)
    }

    @Operation(summary = "어학 시험 목록 조회", description = "지원하는 어학 시험 종류와 점수 범위를 반환합니다.")
    @GetMapping("/exam-types")
    fun getExamTypes(): ResponseEntity<ApiResponse<List<ExamTypeResponse>>> {
        val examTypes = ExamType.entries.map { ExamTypeResponse.from(it) }
        return ApiResponse.success(examTypes)
    }
}
