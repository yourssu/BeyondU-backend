package org.example.beyondubackend.domain.university.unit

import org.assertj.core.api.Assertions.assertThat
import org.example.beyondubackend.domain.university.business.dto.LanguageRequirementResponse
import org.example.beyondubackend.domain.university.business.dto.UniversityDetailResponse
import org.example.beyondubackend.domain.university.implement.University
import org.junit.jupiter.api.Test
import java.util.Base64

class UniversityDetailResponseTest {

    // ── availableMajors 파싱 (availableMajor DB 필드 → 콤마 분리 리스트) ───

    @Test
    fun `availableMajor 콤마 구분 문자열은 트림된 리스트로 변환된다`() {
        val university = makeUniversity(availableMajor = "Arts and Sciences, Business, Engineering")

        val result = UniversityDetailResponse.from(university, emptyList())

        assertThat(result.availableMajors).containsExactly("Arts and Sciences", "Business", "Engineering")
    }

    @Test
    fun `availableMajor 항목 주변의 공백이 제거된다`() {
        val university = makeUniversity(availableMajor = "  Arts and Sciences  ,  Business  ")

        val result = UniversityDetailResponse.from(university, emptyList())

        assertThat(result.availableMajors).containsExactly("Arts and Sciences", "Business")
    }

    @Test
    fun `availableMajor가 null이면 availableMajors가 null로 반환된다`() {
        val university = makeUniversity(availableMajor = null)

        val result = UniversityDetailResponse.from(university, emptyList())

        assertThat(result.availableMajors).isNull()
    }

    @Test
    fun `availableMajor가 공백과 콤마만 있으면 null로 반환된다`() {
        val university = makeUniversity(availableMajor = "  ,  ,  ")

        val result = UniversityDetailResponse.from(university, emptyList())

        assertThat(result.availableMajors).isNull()
    }

    @Test
    fun `availableMajor가 단일 항목이면 단일 원소 리스트로 반환된다`() {
        val university = makeUniversity(availableMajor = "Engineering")

        val result = UniversityDetailResponse.from(university, emptyList())

        assertThat(result.availableMajors).containsExactly("Engineering")
    }

    // ── courseListUrl (availableSubject DB 필드 직접 매핑) ──────────────────

    @Test
    fun `availableSubject URL이 courseListUrl로 그대로 반환된다`() {
        val url = "https://example.edu/courses"
        val university = makeUniversity(availableSubject = url)

        val result = UniversityDetailResponse.from(university, emptyList())

        assertThat(result.courseListUrl).isEqualTo(url)
    }

    @Test
    fun `availableSubject가 null이면 courseListUrl도 null이다`() {
        val university = makeUniversity(availableSubject = null)

        val result = UniversityDetailResponse.from(university, emptyList())

        assertThat(result.courseListUrl).isNull()
    }

    // ── programType 규칙 ────────────────────────────────────────────────────

    @Test
    fun `isExchange true이면 programType이 일반교환이다`() {
        val university = makeUniversity(isExchange = true, isVisit = false)

        val result = UniversityDetailResponse.from(university, emptyList())

        assertThat(result.programType).isEqualTo("일반교환")
    }

    @Test
    fun `isVisit true이면 programType이 방문교환이다`() {
        val university = makeUniversity(isExchange = false, isVisit = true)

        val result = UniversityDetailResponse.from(university, emptyList())

        assertThat(result.programType).isEqualTo("방문교환")
    }

    @Test
    fun `isExchange와 isVisit 모두 false이면 programType이 빈 문자열이다`() {
        val university = makeUniversity(isExchange = false, isVisit = false)

        val result = UniversityDetailResponse.from(university, emptyList())

        assertThat(result.programType).isEqualTo("")
    }

    @Test
    fun `isExchange와 isVisit 모두 true이면 isExchange가 우선되어 programType이 일반교환이다`() {
        val university = makeUniversity(isExchange = true, isVisit = true)

        val result = UniversityDetailResponse.from(university, emptyList())

        assertThat(result.programType).isEqualTo("일반교환")
    }

    // ── reviewReportUrl (#38) ────────────────────────────────────────────────

    @Test
    fun `hasReview true이면 reviewReportUrl이 nameEng Base64 인코딩 URL로 반환된다`() {
        val university = makeUniversity(hasReview = true, nameEng = "Harvard University")

        val result = UniversityDetailResponse.from(university, emptyList())

        val encoded = Base64.getEncoder().encodeToString("Harvard University".toByteArray())
        assertThat(result.reviewReportUrl)
            .isEqualTo("https://study.ssu.ac.kr/community/exp_list.do?searchVal=$encoded&siteCd=01&boardCd=07")
    }

    @Test
    fun `hasReview false이면 reviewReportUrl이 null이다`() {
        val university = makeUniversity(hasReview = false, nameEng = "Harvard University")

        val result = UniversityDetailResponse.from(university, emptyList())

        assertThat(result.reviewReportUrl).isNull()
    }

    @Test
    fun `nameEng에 공백이 포함된 경우에도 Base64 인코딩이 정확하다`() {
        val university = makeUniversity(hasReview = true, nameEng = "University of Melbourne")

        val result = UniversityDetailResponse.from(university, emptyList())

        val encoded = Base64.getEncoder().encodeToString("University of Melbourne".toByteArray())
        assertThat(result.reviewReportUrl)
            .isEqualTo("https://study.ssu.ac.kr/community/exp_list.do?searchVal=$encoded&siteCd=01&boardCd=07")
    }

    // ── location / studentCount 직접 매핑 ──────────────────────────────────

    @Test
    fun `location DB 필드가 location으로 그대로 반환된다`() {
        val university = makeUniversity(location = "Cambridge, MA")

        val result = UniversityDetailResponse.from(university, emptyList())

        assertThat(result.location).isEqualTo("Cambridge, MA")
    }

    @Test
    fun `location이 null이면 location도 null이다`() {
        val university = makeUniversity(location = null)

        val result = UniversityDetailResponse.from(university, emptyList())

        assertThat(result.location).isNull()
    }

    @Test
    fun `studentCount DB 필드가 studentCount로 그대로 반환된다`() {
        val university = makeUniversity(studentCount = "47,000")

        val result = UniversityDetailResponse.from(university, emptyList())

        assertThat(result.studentCount).isEqualTo("47,000")
    }

    @Test
    fun `studentCount가 null이면 null로 반환된다`() {
        val university = makeUniversity(studentCount = null)

        val result = UniversityDetailResponse.from(university, emptyList())

        assertThat(result.studentCount).isNull()
    }

    // ── languageRequirements 전달 ────────────────────────────────────────────

    @Test
    fun `전달된 languageRequirements 리스트가 그대로 포함된다`() {
        val university = makeUniversity()
        val langReqs = listOf(
            LanguageRequirementResponse(languageGroup = "영어", examType = "TOEFL iBT", minScore = 80.0)
        )

        val result = UniversityDetailResponse.from(university, langReqs)

        assertThat(result.languageRequirements).hasSize(1)
        assertThat(result.languageRequirements[0].examType).isEqualTo("TOEFL iBT")
    }

    // ── 헬퍼 ────────────────────────────────────────────────────────────────

    private fun makeUniversity(
        id: Long = 1L,
        isExchange: Boolean = true,
        isVisit: Boolean = false,
        hasReview: Boolean = false,
        nameEng: String = "Test University",
        availableMajor: String? = null,
        availableSubject: String? = null,
        location: String? = null,
        studentCount: String? = null
    ) = University(
        id = id,
        semester = "2024-1",
        region = "미주",
        nation = "USA",
        nameKor = "테스트대학교",
        nameEng = nameEng,
        minGpa = 3.0,
        remark = "특이사항 없음",
        isExchange = isExchange,
        isVisit = isVisit,
        hasReview = hasReview,
        availableMajor = availableMajor,
        availableSubject = availableSubject,
        location = location,
        studentCount = studentCount
    )
}
