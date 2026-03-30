package org.example.beyondubackend.domain.university.flow

import org.assertj.core.api.Assertions.assertThat
import org.example.beyondubackend.domain.languagerequirement.storage.LanguageRequirementEntity
import org.example.beyondubackend.domain.languagerequirement.storage.LanguageRequirementJpaRepository
import org.example.beyondubackend.domain.university.storage.UniversityEntity
import org.example.beyondubackend.domain.university.storage.UniversityJpaRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UniversityFlowTest {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var universityJpaRepository: UniversityJpaRepository

    @Autowired
    private lateinit var languageRequirementJpaRepository: LanguageRequirementJpaRepository

    private val baseUrl get() = "http://localhost:$port/api/v1/universities"

    // ── 픽스처 ──────────────────────────────────────────────────────────────
    // uni1: USA, GPA 3.0, 교환, TOEFL iBT 80 / IELTS 6.0, 후기 있음
    // uni2: JPN, GPA 3.5, 교환, JLPT N2(minScore=2)
    // uni3: USA, GPA 2.5, 방문, 어학 요구사항 없음
    // uni4: CHN, GPA 3.8, 교환, TOEFL iBT 100, 후기 있음
    // uni5: AUS, GPA 2.0, 교환, 어학 요구사항 없음

    private lateinit var uni1: UniversityEntity
    private lateinit var uni2: UniversityEntity
    private lateinit var uni3: UniversityEntity
    private lateinit var uni4: UniversityEntity
    private lateinit var uni5: UniversityEntity

    @BeforeEach
    fun setUp() {
        languageRequirementJpaRepository.deleteAll()
        universityJpaRepository.deleteAll()

        uni1 = universityJpaRepository.save(makeUniversity(
            nameKor = "하버드대학교", nameEng = "Harvard University",
            nation = "USA", minGpa = 3.0,
            isExchange = true, isVisit = false,
            hasReview = true, reviewYear = "2024",
            availableMajor = "Arts and Sciences, Business",
            location = "Cambridge, MA", studentCount = "47,000"
        ))
        uni2 = universityJpaRepository.save(makeUniversity(
            nameKor = "도쿄대학교", nameEng = "University of Tokyo",
            nation = "JPN", minGpa = 3.5,
            isExchange = true, isVisit = false,
            availableMajor = "Engineering",
            location = "Tokyo, Japan", studentCount = "28,000"
        ))
        uni3 = universityJpaRepository.save(makeUniversity(
            nameKor = "캘리포니아주립대", nameEng = "California State University",
            nation = "USA", minGpa = 2.5,
            isExchange = false, isVisit = true,
            location = "Los Angeles, CA"
        ))
        uni4 = universityJpaRepository.save(makeUniversity(
            nameKor = "베이징대학교", nameEng = "Peking University",
            nation = "CHN", minGpa = 3.8,
            isExchange = true, isVisit = false,
            hasReview = true, reviewYear = "2023",
            availableMajor = "Business, Economics",
            location = "Beijing, China", studentCount = "55,000"
        ))
        uni5 = universityJpaRepository.save(makeUniversity(
            nameKor = "멜버른대학교", nameEng = "University of Melbourne",
            nation = "AUS", minGpa = 2.0,
            isExchange = true, isVisit = false
        ))

        languageRequirementJpaRepository.saveAll(listOf(
            makeLangReq(uni1.id!!, "영어", "TOEFL iBT", 80.0),
            makeLangReq(uni1.id!!, "영어", "IELTS", 6.0),
            makeLangReq(uni2.id!!, "일본어", "JLPT", 2.0),
            makeLangReq(uni4.id!!, "영어", "TOEFL iBT", 100.0)
        ))
    }

    @AfterEach
    fun tearDown() {
        languageRequirementJpaRepository.deleteAll()
        universityJpaRepository.deleteAll()
    }

    // ── 전체 조회: 파라미터 없음 ────────────────────────────────────────────

    @Test
    fun `파라미터 없이 전체 조회 시 5개 대학이 모두 반환된다`() {
        val response = get<Map<String, Any?>>(baseUrl)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        val universities = universities(response)
        assertThat(universities).hasSize(5)
    }

    // ── 전체 조회: GPA 필터 ─────────────────────────────────────────────────

    @Test
    fun `GPA 3점 입력 시 minGpa 3점 이하 대학(uni1 uni3 uni5)만 반환된다`() {
        val response = get<Map<String, Any?>>("$baseUrl?gpa=3.0")

        val ids = universityIds(response)
        assertThat(ids).containsExactlyInAnyOrder(uni1.id, uni3.id, uni5.id)
    }

    @Test
    fun `GPA 3점5 입력 시 minGpa 3점5 이하 대학 4개가 반환된다`() {
        val response = get<Map<String, Any?>>("$baseUrl?gpa=3.5")

        val ids = universityIds(response)
        assertThat(ids).containsExactlyInAnyOrder(uni1.id, uni2.id, uni3.id, uni5.id)
    }

    @Test
    fun `내 GPA보다 요구 GPA가 높은 대학은 결과에 포함되지 않는다`() {
        val response = get<Map<String, Any?>>("$baseUrl?gpa=2.0")

        val ids = universityIds(response)
        assertThat(ids).containsOnly(uni5.id)
    }

    // ── 전체 조회: 어학 점수 단독 필터 ─────────────────────────────────────

    @Test
    fun `TOEFL IBT 80점 보유 시 충족 대학과 어학 요구사항 없는 대학이 반환된다`() {
        val response = get<Map<String, Any?>>("$baseUrl?TOEFL_IBT=80")

        val ids = universityIds(response)
        // uni1: TOEFL iBT 80 충족, uni3/uni5: 어학 요구사항 없음
        assertThat(ids).containsExactlyInAnyOrder(uni1.id, uni3.id, uni5.id)
    }

    @Test
    fun `보유 TOEFL 점수가 대학 요구 점수보다 낮으면 해당 대학은 제외된다`() {
        val response = get<Map<String, Any?>>("$baseUrl?TOEFL_IBT=79")

        val ids = universityIds(response)
        // uni1 TOEFL 80 요구 → 79 < 80 → 제외, uni4 TOEFL 100 요구 → 제외
        assertThat(ids).doesNotContain(uni1.id, uni4.id)
        assertThat(ids).containsExactlyInAnyOrder(uni3.id, uni5.id)
    }

    @Test
    fun `어학 요구사항이 없는 대학은 어학 필터와 무관하게 항상 조회된다`() {
        val response = get<Map<String, Any?>>("$baseUrl?TOEFL_IBT=120")

        val ids = universityIds(response)
        assertThat(ids).contains(uni3.id, uni5.id)
    }

    // ── 전체 조회: GPA + 어학 복합 필터 ────────────────────────────────────

    @Test
    fun `GPA 3점5 + TOEFL IBT 80점 조합 시 조건을 모두 충족하는 대학만 반환된다`() {
        val response = get<Map<String, Any?>>("$baseUrl?gpa=3.5&TOEFL_IBT=80")

        val ids = universityIds(response)
        // uni1(GPA 3.0 ≤ 3.5, TOEFL 80 충족), uni3(GPA 2.5, 어학없음), uni5(GPA 2.0, 어학없음)
        // uni2(GPA 3.5, JLPT만 있어 TOEFL 불충족), uni4(GPA 3.8 > 3.5)
        assertThat(ids).containsExactlyInAnyOrder(uni1.id, uni3.id, uni5.id)
    }

    @Test
    fun `GPA 3점5 + 어학 2개(OR 조건) 입력 시 하나라도 충족하면 포함된다`() {
        val response = get<Map<String, Any?>>("$baseUrl?gpa=3.5&TOEFL_IBT=80&IELTS=5.0")

        val ids = universityIds(response)
        // uni1: TOEFL 80 충족(IELTS 6.0은 불충족이지만 OR이므로 포함)
        // uni2: JLPT만 있어 TOEFL/IELTS 불충족 → 제외
        assertThat(ids).containsExactlyInAnyOrder(uni1.id, uni3.id, uni5.id)
    }

    // ── 전체 조회: 국가 + GPA + 어학 복합 필터 ─────────────────────────────

    @Test
    fun `나라 USA + GPA 3점5 + TOEFL IBT 80점 조합 시 조건 모두 충족하는 미국 대학만 반환된다`() {
        val response = get<Map<String, Any?>>("$baseUrl?nation=USA&gpa=3.5&TOEFL_IBT=80")

        val ids = universityIds(response)
        // USA: uni1, uni3 / GPA: uni1(3.0), uni3(2.5) / 어학: uni1 TOEFL 충족, uni3 없음
        assertThat(ids).containsExactlyInAnyOrder(uni1.id, uni3.id)
    }

    @Test
    fun `나라 USA + GPA 3점5 + 어학 2개(OR) 조합 시 미국 대학 중 조건 충족 반환`() {
        val response = get<Map<String, Any?>>("$baseUrl?nation=USA&gpa=3.5&TOEFL_IBT=80&IELTS=6.0")

        val ids = universityIds(response)
        assertThat(ids).containsExactlyInAnyOrder(uni1.id, uni3.id)
    }

    @Test
    fun `나라 JPN + GPA 4점0 + JLPT 2 조합 시 도쿄대가 반환된다`() {
        val response = get<Map<String, Any?>>("$baseUrl?nation=JPN&gpa=4.0&JLPT=2")

        val ids = universityIds(response)
        assertThat(ids).containsExactlyInAnyOrder(uni2.id)
    }

    // ── 전체 조회: JLPT 역방향 필터 ────────────────────────────────────────

    @Test
    fun `JLPT N1(레벨1) 보유 시 N2 요구 대학도 지원 가능하다`() {
        val response = get<Map<String, Any?>>("$baseUrl?JLPT=1")

        val ids = universityIds(response)
        // uni2: JLPT N2(minScore=2), DB:2 >= user:1 → 충족
        assertThat(ids).contains(uni2.id)
    }

    @Test
    fun `JLPT N3(레벨3) 보유 시 N2 요구 대학에 지원 불가능하다`() {
        val response = get<Map<String, Any?>>("$baseUrl?JLPT=3")

        val ids = universityIds(response)
        // uni2: DB:2 >= user:3 → false → 제외
        assertThat(ids).doesNotContain(uni2.id)
        assertThat(ids).containsExactlyInAnyOrder(uni3.id, uni5.id)
    }

    // ── 전체 조회: 기타 단독 필터 ──────────────────────────────────────────

    @Test
    fun `국가 USA 필터 시 미국 대학만 반환된다`() {
        val response = get<Map<String, Any?>>("$baseUrl?nation=USA")

        val ids = universityIds(response)
        assertThat(ids).containsExactlyInAnyOrder(uni1.id, uni3.id)
    }

    @Test
    fun `isExchange true 필터 시 일반교환 대학만 반환된다`() {
        val response = get<Map<String, Any?>>("$baseUrl?isExchange=true")

        val ids = universityIds(response)
        assertThat(ids).containsExactlyInAnyOrder(uni1.id, uni2.id, uni4.id, uni5.id)
        assertThat(ids).doesNotContain(uni3.id)
    }

    @Test
    fun `isVisit true 필터 시 방문교환 대학만 반환된다`() {
        val response = get<Map<String, Any?>>("$baseUrl?isVisit=true")

        val ids = universityIds(response)
        assertThat(ids).containsExactlyInAnyOrder(uni3.id)
    }

    @Test
    fun `hasReview true 필터 시 후기 있는 대학만 반환된다`() {
        val response = get<Map<String, Any?>>("$baseUrl?hasReview=true")

        val ids = universityIds(response)
        assertThat(ids).containsExactlyInAnyOrder(uni1.id, uni4.id)
    }

    @Test
    fun `search 키워드로 한국어 대학명 검색 시 해당 대학이 반환된다`() {
        val response = get<Map<String, Any?>>("$baseUrl?search=하버드")

        val ids = universityIds(response)
        assertThat(ids).containsExactlyInAnyOrder(uni1.id)
    }

    @Test
    fun `search 키워드로 영어 대학명 검색 시 해당 대학이 반환된다`() {
        val response = get<Map<String, Any?>>("$baseUrl?search=Melbourne")

        val ids = universityIds(response)
        assertThat(ids).containsExactlyInAnyOrder(uni5.id)
    }

    @Test
    fun `major 키워드로 수강 가능 학과 필터 시 해당 학과 포함 대학만 반환된다`() {
        val response = get<Map<String, Any?>>("$baseUrl?major=Business")

        val ids = universityIds(response)
        // uni1("Arts and Sciences, Business"), uni4("Business, Economics") 포함
        assertThat(ids).containsExactlyInAnyOrder(uni1.id, uni4.id)
    }

    // ── 전체 조회: 빈 결과 / 경계값 ────────────────────────────────────────

    @Test
    fun `일치하는 대학이 없으면 빈 리스트와 totalElements 0이 반환된다`() {
        val response = get<Map<String, Any?>>("$baseUrl?nation=DEU")

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        val result = result(response)
        val universities = result["universities"] as List<*>
        val pageInfo = result["pageInfo"] as Map<*, *>
        assertThat(universities).isEmpty()
        assertThat(pageInfo["totalElements"]).isEqualTo(0)
    }

    @Test
    fun `페이지 크기 2로 요청 시 2개만 반환되고 pageInfo가 올바르게 계산된다`() {
        val response = get<Map<String, Any?>>("$baseUrl?size=2&page=0")

        val result = result(response)
        val universities = result["universities"] as List<*>
        val pageInfo = result["pageInfo"] as Map<*, *>
        assertThat(universities).hasSize(2)
        assertThat(pageInfo["totalElements"]).isEqualTo(5)
        assertThat(pageInfo["isLast"]).isEqualTo(false)
    }

    @Test
    fun `마지막 페이지에서는 isLast가 true로 반환된다`() {
        val response = get<Map<String, Any?>>("$baseUrl?size=3&page=1")

        val result = result(response)
        val pageInfo = result["pageInfo"] as Map<*, *>
        assertThat(pageInfo["isLast"]).isEqualTo(true)
    }

    // ── 전체 조회: 어학 점수 유효 범위 초과 ────────────────────────────────

    @Test
    fun `TOEFL IBT 점수가 120 초과이면 400 에러가 반환된다`() {
        val response = get<Map<String, Any?>>( "$baseUrl?TOEFL_IBT=999")

        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `JLPT 레벨이 5 초과이면 400 에러가 반환된다`() {
        val response = get<Map<String, Any?>>("$baseUrl?JLPT=6")

        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    // ── 상세 조회: 정상 ─────────────────────────────────────────────────────

    @Test
    fun `존재하는 대학 ID로 상세 조회 시 올바른 데이터가 반환된다`() {
        val response = get<Map<String, Any?>>("$baseUrl/${uni1.id}")

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        val detail = result(response)
        assertThat(detail["nameKor"]).isEqualTo("하버드대학교")
        assertThat(detail["programType"]).isEqualTo("일반교환")
        assertThat(detail["location"]).isEqualTo("Cambridge, MA")
        assertThat(detail["studentCount"]).isEqualTo("47,000")
    }

    @Test
    fun `상세 조회 시 availableMajors가 콤마 기준으로 리스트로 반환된다`() {
        val response = get<Map<String, Any?>>("$baseUrl/${uni1.id}")

        val detail = result(response)
        @Suppress("UNCHECKED_CAST")
        val majors = detail["availableMajors"] as List<String>
        assertThat(majors).containsExactlyInAnyOrder("Arts and Sciences", "Business")
    }

    @Test
    fun `상세 조회 시 courseListUrl은 availableSubject 필드 값이다`() {
        val url = "https://example.com/courses"
        val saved = universityJpaRepository.save(makeUniversity(
            nameKor = "URL테스트대학", nameEng = "URL Test University",
            availableSubject = url
        ))

        val response = get<Map<String, Any?>>("$baseUrl/${saved.id}")

        val detail = result(response)
        assertThat(detail["courseListUrl"]).isEqualTo(url)
    }

    @Test
    fun `방문교환 대학 상세 조회 시 programType이 방문교환이다`() {
        val response = get<Map<String, Any?>>("$baseUrl/${uni3.id}")

        val detail = result(response)
        assertThat(detail["programType"]).isEqualTo("방문교환")
    }

    @Test
    fun `어학 요구사항이 있는 대학 상세 조회 시 languageRequirements에 해당 정보가 포함된다`() {
        val response = get<Map<String, Any?>>("$baseUrl/${uni1.id}")

        val detail = result(response)
        @Suppress("UNCHECKED_CAST")
        val langReqs = detail["languageRequirements"] as List<Map<String, Any?>>
        assertThat(langReqs).hasSize(2)
        val examTypes = langReqs.map { it["examType"] }
        assertThat(examTypes).containsExactlyInAnyOrder("TOEFL iBT", "IELTS")
    }

    @Test
    fun `어학 요구사항이 없는 대학 상세 조회 시 languageRequirements가 빈 리스트다`() {
        val response = get<Map<String, Any?>>("$baseUrl/${uni5.id}")

        val detail = result(response)
        @Suppress("UNCHECKED_CAST")
        val langReqs = detail["languageRequirements"] as List<*>
        assertThat(langReqs).isEmpty()
    }

    // ── 상세 조회: 에러 ─────────────────────────────────────────────────────

    @Test
    fun `존재하지 않는 대학 ID로 상세 조회 시 404가 반환된다`() {
        val response = get<Map<String, Any?>>("$baseUrl/999999")

        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        val body = response.body!!
        assertThat(body["code"]).isEqualTo("U404_001")
    }

    @Test
    fun `존재하지 않는 엔드포인트 호출 시 404가 반환된다`() {
        val response = get<Map<String, Any?>>("$baseUrl/999999/unknown")

        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

    // ── 헬퍼 ────────────────────────────────────────────────────────────────

    @Suppress("UNCHECKED_CAST")
    private fun <T> get(url: String) =
        restTemplate.getForEntity(url, Map::class.java) as org.springframework.http.ResponseEntity<Map<String, Any?>>

    @Suppress("UNCHECKED_CAST")
    private fun result(response: org.springframework.http.ResponseEntity<Map<String, Any?>>) =
        response.body!!["result"] as Map<String, Any?>

    @Suppress("UNCHECKED_CAST")
    private fun universities(response: org.springframework.http.ResponseEntity<Map<String, Any?>>) =
        (result(response)["universities"] as List<Map<String, Any?>>)

    private fun universityIds(response: org.springframework.http.ResponseEntity<Map<String, Any?>>): List<Long> {
        return universities(response).map { (it["id"] as Number).toLong() }
    }

    private fun makeUniversity(
        nameKor: String = "테스트대학",
        nameEng: String = "Test University",
        nation: String = "USA",
        minGpa: Double = 3.0,
        isExchange: Boolean = true,
        isVisit: Boolean = false,
        hasReview: Boolean = false,
        reviewYear: String? = null,
        availableMajor: String? = null,
        availableSubject: String? = null,
        location: String? = null,
        studentCount: String? = null
    ) = UniversityEntity(
        semester = "2024-1",
        region = "미주",
        nation = nation,
        nameKor = nameKor,
        nameEng = nameEng,
        minGpa = minGpa,
        remark = "특이사항 없음",
        isExchange = isExchange,
        isVisit = isVisit,
        hasReview = hasReview,
        reviewYear = reviewYear,
        availableMajor = availableMajor,
        availableSubject = availableSubject,
        location = location,
        studentCount = studentCount
    )

    private fun makeLangReq(
        universityId: Long,
        languageGroup: String,
        examType: String,
        minScore: Double,
        levelCode: String? = null
    ) = LanguageRequirementEntity(
        universityId = universityId,
        languageGroup = languageGroup,
        examType = examType,
        minScore = minScore,
        levelCode = levelCode
    )
}
