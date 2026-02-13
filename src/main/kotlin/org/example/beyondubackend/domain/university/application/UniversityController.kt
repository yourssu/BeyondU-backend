package org.example.beyondubackend.domain.university.application

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

@RestController
@RequestMapping("/api/v1/universities")
class UniversityController(
    private val universityService: UniversityService
) {

    @GetMapping
    fun getUniversities(
        @ModelAttribute request: UniversitySearchRequest,
        @ExamScoreParams examScores: Map<String, Double>,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "12") size: Int
    ): ResponseEntity<ApiResponse<UniversityListResponse>> {
        val pageable = PageRequest.of(page, size, Sort.by("nameEng").ascending())
        val query = request.toQuery(examScores)
        val result = universityService.getUniversities(query, pageable)
        return ApiResponse.success(result)
    }

    @GetMapping("/{id}")
    fun getUniversityDetail(
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse<UniversityDetailResponse>> {
        val result = universityService.getUniversityDetail(id)
        return ApiResponse.success(result)
    }
}
