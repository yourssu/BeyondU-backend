package org.example.beyondubackend.domain.university.unit

import org.assertj.core.api.Assertions.assertThat
import org.example.beyondubackend.domain.languagerequirement.implement.LanguageRequirement
import org.example.beyondubackend.domain.languagerequirement.implement.LanguageRequirementReader
import org.example.beyondubackend.domain.languagerequirement.implement.LanguageRequirementRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class LanguageRequirementSummaryTest {
    @Mock
    private lateinit var languageRequirementRepository: LanguageRequirementRepository

    @InjectMocks
    private lateinit var reader: LanguageRequirementReader

    // ── generateSummary ─────────────────────────────────────────────────────

    @Test
    fun `빈 리스트이면 null을 반환한다`() {
        val result = reader.generateSummary(emptyList())

        assertThat(result).isNull()
    }

    @Test
    fun `단일 TOEFL iBT 요구사항은 점수가 정수이면 정수 형태로 표시된다`() {
        val requirements = listOf(req("TOEFL iBT", 100.0))

        val result = reader.generateSummary(requirements)

        assertThat(result).isEqualTo("TOEFL iBT 100")
    }

    @Test
    fun `점수가 소수이면 소수점까지 그대로 표시된다`() {
        val requirements = listOf(req("IELTS", 6.5))

        val result = reader.generateSummary(requirements)

        assertThat(result).isEqualTo("IELTS 6.5")
    }

    @Test
    fun `복수 요구사항은 슬래시로 구분하여 모두 표시된다`() {
        val requirements =
            listOf(
                req("TOEFL iBT", 100.0),
                req("IELTS", 7.0),
            )

        val result = reader.generateSummary(requirements)

        // 소수점이 0이면 정수로 포맷(7.0 → "7")
        assertThat(result).isEqualTo("TOEFL iBT 100 / IELTS 7")
    }

    @Test
    fun `HSK는 levelCode와 무관하게 suffix(급)가 붙어서 표시된다`() {
        val requirements = listOf(req("HSK", 4.0))

        val result = reader.generateSummary(requirements)

        assertThat(result).isEqualTo("HSK 4급")
    }

    @Test
    fun `JLPT는 prefix N이 붙어서 표시된다`() {
        val requirements = listOf(req("JLPT", 2.0))

        val result = reader.generateSummary(requirements)

        assertThat(result).isEqualTo("JLPT N2")
    }

    @Test
    fun `세 개 이상 요구사항도 슬래시로 올바르게 연결된다`() {
        val requirements =
            listOf(
                req("TOEFL iBT", 80.0),
                req("TOEIC", 700.0),
                req("IELTS", 6.0),
            )

        val result = reader.generateSummary(requirements)

        // 소수점이 0이면 정수로 포맷(6.0 → "6")
        assertThat(result).isEqualTo("TOEFL iBT 80 / TOEIC 700 / IELTS 6")
    }

    // ── 헬퍼 ────────────────────────────────────────────────────────────────

    private fun req(
        examType: String,
        minScore: Double,
        levelCode: String? = null,
    ) = LanguageRequirement(
        universityId = 1L,
        languageGroup = "영어",
        examType = examType,
        minScore = minScore,
        levelCode = levelCode,
    )
}
