package org.example.beyondubackend.domain.meta.application.dto

import org.example.beyondubackend.common.enums.ExamType
import org.example.beyondubackend.common.enums.ExamValueSchema

data class ExamTypeSchemaResponse(
    val name: String,
    val value: ExamValueSchema,
) {
    companion object {
        fun from(examType: ExamType) =
            ExamTypeSchemaResponse(
                name = examType.paramName,
                value = examType.schema,
            )
    }
}
