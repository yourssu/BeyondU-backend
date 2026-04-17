package org.example.beyondubackend.domain.meta.business

import org.example.beyondubackend.common.enums.KoreanMajor
import org.example.beyondubackend.common.enums.MajorCategory
import org.example.beyondubackend.common.enums.Region
import org.example.beyondubackend.common.enums.SubMajor
import org.example.beyondubackend.domain.meta.application.dto.MajorCategoryResponse
import org.example.beyondubackend.domain.meta.application.dto.NationsByRegionResponse
import org.example.beyondubackend.domain.meta.application.dto.SubMajorResponse
import org.example.beyondubackend.domain.university.implement.UniversityReader
import org.springframework.stereotype.Service

@Service
class MetaServiceImpl(
    private val universityReader: UniversityReader,
) : MetaService {
    override fun getNationsByRegion(): List<NationsByRegionResponse> {
        val regionOrder = Region.entries.map { it.displayName }
        return universityReader.getDistinctRegionAndNation()
            .groupBy { it[0] }
            .entries
            .sortedBy { (region, _) -> regionOrder.indexOf(region).takeIf { it >= 0 } ?: Int.MAX_VALUE }
            .map { (region, pairs) ->
                NationsByRegionResponse(
                    region = region,
                    nations = pairs.map { it[1] },
                )
            }
    }

    override fun getMajors(): List<MajorCategoryResponse> {
        val koreanMajorsBySubMajor = KoreanMajor.entries
            .flatMap { km -> km.subMajors.map { sub -> sub to km.displayName } }
            .groupBy({ it.first }, { it.second })

        return MajorCategory.entries.map { cat ->
            MajorCategoryResponse(
                category = cat.displayName,
                majors = SubMajor.entries
                    .filter { it.category == cat }
                    .map { sub ->
                        SubMajorResponse(
                            enumName = sub.name,
                            name = sub.displayName,
                            koreanMajors = koreanMajorsBySubMajor[sub] ?: emptyList(),
                        )
                    },
            )
        }
    }
}
