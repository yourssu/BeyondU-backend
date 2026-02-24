package org.example.beyondubackend.domain.languagerequirement.implement

enum class ExamType(
    val paramName: String,
    val displayName: String,
    val minScore: Double,
    val maxScore: Double
) {
    TOEFL_IBT("TOEFL_IBT", "TOEFL iBT", 0.0, 120.0),
    TOEFL_ITP("TOEFL_ITP", "TOEFL ITP", 0.0, 677.0),
    IELTS("IELTS", "IELTS", 0.0, 9.0),
    TOEIC("TOEIC", "TOEIC", 0.0, 990.0),
    TOEIC_SPEAKING("TOEIC_Speaking", "TOEIC Speaking", 0.0, 200.0),
    HSK("HSK", "HSK", 1.0, 6.0),
    JLPT("JLPT", "JLPT", 1.0, 5.0),
    JPT("JPT", "JPT", 0.0, 990.0),
    DELF("DELF", "DELF", 1.0, 6.0),
    ZD("ZD", "ZD", 1.0, 6.0);

    companion object {
        fun fromParamName(paramName: String): ExamType? = entries.find { it.paramName == paramName }
        fun fromDisplayName(displayName: String): ExamType? = entries.find { it.displayName == displayName }
    }
}
