package org.example.beyondubackend.common.enums

enum class ExamType(
    val paramName: String,
    val displayName: String,
    val minScore: Double,
    val maxScore: Double,
    val prefix: String = "",
    val suffix: String = "",
    val schema: ExamValueSchema,
) {
    TOEFL_IBT("TOEFL_IBT", "TOEFL iBT", 0.0, 120.0, schema = ExamValueSchema.NumberSchema(0.0, 120.0, 1.0)),
    TOEFL_ITP("TOEFL_ITP", "TOEFL ITP", 0.0, 677.0, schema = ExamValueSchema.NumberSchema(0.0, 677.0, 1.0)),
    IELTS("IELTS", "IELTS", 0.0, 9.0, schema = ExamValueSchema.NumberSchema(0.0, 9.0, 0.5)),
    TOEIC("TOEIC", "TOEIC", 0.0, 990.0, schema = ExamValueSchema.NumberSchema(0.0, 990.0, 5.0)),
    TOEIC_SPEAKING("TOEIC_Speaking", "TOEIC Speaking", 0.0, 200.0, schema = ExamValueSchema.NumberSchema(0.0, 200.0, 10.0)),
    HSK("HSK", "HSK", 1.0, 6.0, suffix = "급", schema = ExamValueSchema.StringEnumSchema(listOf("1급", "2급", "3급", "4급", "5급", "6급"))),
    JLPT("JLPT", "JLPT", 1.0, 5.0, prefix = "N", schema = ExamValueSchema.StringEnumSchema(listOf("N1", "N2", "N3", "N4", "N5"))),
    JPT("JPT", "JPT", 0.0, 990.0, schema = ExamValueSchema.NumberSchema(0.0, 990.0, 5.0)),
    DELF("DELF", "DELF", 1.0, 6.0, schema = ExamValueSchema.StringEnumSchema(listOf("A1", "A2", "B1", "B2", "C1", "C2"))),
    ZD("ZD", "ZD", 1.0, 6.0, schema = ExamValueSchema.StringEnumSchema(listOf("A1", "A2", "B1", "B2", "C1", "C2"))),
    ;

    fun parseQueryValue(raw: String): Double? =
        when (val s = schema) {
            is ExamValueSchema.NumberSchema -> raw.toDoubleOrNull()
            is ExamValueSchema.StringEnumSchema -> {
                val idx = s.enum.indexOf(raw)
                if (idx >= 0) minScore + idx else null
            }
        }

    companion object {
        fun fromParamName(paramName: String): ExamType? = entries.find { it.paramName == paramName }

        fun fromDisplayName(displayName: String): ExamType? = entries.find { it.displayName == displayName }
    }
}
