package org.example.beyondubackend.domain.meta.application

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.example.beyondubackend.common.dto.ApiResponse
import org.example.beyondubackend.common.enums.ExamType
import org.example.beyondubackend.domain.meta.application.dto.ExamTypeSchemaResponse
import org.example.beyondubackend.domain.meta.application.dto.NationsByRegionResponse
import org.example.beyondubackend.domain.meta.business.MetaService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Meta", description = "공통 코드 조회 API")
@RestController
@RequestMapping("/api/v2/meta")
class MetaV2Controller(
    private val metaService: MetaService,
) {
    @Operation(summary = "국가 목록 조회 (지역별 그룹핑)", description = "대륙별로 그룹핑된 국가 목록을 반환합니다.")
    @GetMapping("/nations")
    fun getNations(): ResponseEntity<ApiResponse<List<NationsByRegionResponse>>> =
        ApiResponse.success(metaService.getNationsByRegion())

    @Operation(summary = "어학 시험 목록 조회 (inputSchema 포함)")
    @GetMapping("/exam-types")
    fun getExamTypes(): ResponseEntity<ApiResponse<List<ExamTypeSchemaResponse>>> =
        ApiResponse.success(ExamType.entries.map { ExamTypeSchemaResponse.from(it) })
}
