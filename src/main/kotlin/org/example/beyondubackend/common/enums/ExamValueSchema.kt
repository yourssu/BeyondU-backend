package org.example.beyondubackend.common.enums

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
@JsonSubTypes(
    JsonSubTypes.Type(value = ExamValueSchema.NumberSchema::class, name = "number"),
    JsonSubTypes.Type(value = ExamValueSchema.StringEnumSchema::class, name = "string"),
)
sealed interface ExamValueSchema {
    val type: String

    data class NumberSchema(
        val min: Double,
        val max: Double,
        val multipleOf: Double,
    ) : ExamValueSchema {
        override val type: String = "number"
    }

    data class StringEnumSchema(
        val enum: List<String>,
    ) : ExamValueSchema {
        override val type: String = "string"
    }
}
