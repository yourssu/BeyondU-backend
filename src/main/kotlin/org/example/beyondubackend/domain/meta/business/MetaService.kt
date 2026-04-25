package org.example.beyondubackend.domain.meta.business

import org.example.beyondubackend.domain.meta.application.dto.MajorCategoryResponse
import org.example.beyondubackend.domain.meta.application.dto.NationsByRegionResponse

interface MetaService {
    fun getNations(): List<String>

    fun getNationsByRegion(): List<NationsByRegionResponse>

    fun getMajors(): List<MajorCategoryResponse>
}
