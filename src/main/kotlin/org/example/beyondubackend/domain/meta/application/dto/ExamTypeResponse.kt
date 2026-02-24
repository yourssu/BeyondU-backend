package org.example.beyondubackend.domain.meta.application.dto

import org.example.beyondubackend.common.enums.ExamType

data class ExamTypeResponse(
    val paramName: String,
    val displayName: String,
    val minScore: Double,
    val maxScore: Double
) {
    companion object {
        fun from(examType: ExamType) = ExamTypeResponse(
            paramName = examType.paramName,
            displayName = examType.displayName,
            minScore = examType.minScore,
            maxScore = examType.maxScore
        )
    }
}
