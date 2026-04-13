package org.example.beyondubackend.domain.meta.application

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.example.beyondubackend.common.dto.ApiResponse
import org.example.beyondubackend.common.enums.*
import org.example.beyondubackend.domain.meta.application.dto.ExamTypeResponse
import org.example.beyondubackend.domain.meta.application.dto.MajorCategoryResponse
import org.example.beyondubackend.domain.meta.application.dto.NationsByRegionResponse
import org.example.beyondubackend.domain.meta.application.dto.SubMajorResponse
import org.example.beyondubackend.domain.university.storage.UniversityJpaRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Meta", description = "공통 코드 조회 API")
@RestController
@RequestMapping("/api/v1/meta")
class MetaController(
    private val universityJpaRepository: UniversityJpaRepository,
) {
    @Operation(summary = "국가 목록 조회 (지역별 그룹핑)", description = "대륙별로 그룹핑된 국가 목록을 반환합니다.")
    @GetMapping("/nations")
    fun getNations(): ResponseEntity<ApiResponse<List<NationsByRegionResponse>>> {
        val regionOrder = Region.entries.map { it.displayName }
        val grouped = universityJpaRepository.findDistinctRegionAndNation()
            .groupBy { it[0] }
            .entries
            .sortedBy { (region, _) -> regionOrder.indexOf(region).takeIf { it >= 0 } ?: Int.MAX_VALUE }
            .map { (region, pairs) ->
                NationsByRegionResponse(
                    region = region,
                    nations = pairs.map { it[1] },
                )
            }
        return ApiResponse.success(grouped)
    }

    @Operation(summary = "전공 목록 조회 (카테고리별 그룹핑)", description = "카테고리별로 그룹핑된 전공 목록을 반환합니다.")
    @GetMapping("/majors")
    fun getMajors(): ResponseEntity<ApiResponse<List<MajorCategoryResponse>>> {
        val koreanMajorsBySubMajor = KoreanMajor.entries
            .flatMap { km -> km.subMajors.map { sub -> sub to km.displayName } }
            .groupBy({ it.first }, { it.second })

        val result = MajorCategory.entries.map { cat ->
            MajorCategoryResponse(
                category = cat.displayName,
                majors = SubMajor.entries
                    .filter { it.category == cat }
                    .map { sub ->
                        SubMajorResponse(
                            name = sub.displayName,
                            koreanMajors = koreanMajorsBySubMajor[sub] ?: emptyList(),
                        )
                    },
            )
        }
        return ApiResponse.success(result)
    }

    @Operation(summary = "대륙 목록 조회", description = "university 필터링에 사용 가능한 대륙 목록을 반환합니다.")
    @GetMapping("/regions")
    fun getRegions(): ResponseEntity<ApiResponse<List<String>>> {
        val regions = Region.entries.map { it.displayName }
        return ApiResponse.success(regions)
    }

    @Operation(summary = "언어권 목록 조회", description = "university 필터링에 사용 가능한 언어권 목록을 반환합니다.")
    @GetMapping("/language-groups")
    fun getLanguageGroups(): ResponseEntity<ApiResponse<List<String>>> {
        val languageGroups = LanguageGroup.entries.map { it.name }
        return ApiResponse.success(languageGroups)
    }

    @Operation(summary = "어학 시험 목록 조회", description = "지원하는 어학 시험 종류와 점수 범위를 반환합니다.")
    @GetMapping("/exam-types")
    fun getExamTypes(): ResponseEntity<ApiResponse<List<ExamTypeResponse>>> {
        val examTypes = ExamType.entries.map { ExamTypeResponse.from(it) }
        return ApiResponse.success(examTypes)
    }
}
