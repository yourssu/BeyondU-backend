package org.example.beyondubackend.common.resolver

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.example.beyondubackend.common.annotation.ExamScoreParams
import org.example.beyondubackend.common.exception.BusinessException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.core.MethodParameter
import org.springframework.web.context.request.NativeWebRequest

@ExtendWith(MockitoExtension::class)
class ExamScoreArgumentResolverTest {
    private val resolver = ExamScoreArgumentResolver()

    @Mock
    private lateinit var webRequest: NativeWebRequest

    @Mock
    private lateinit var parameter: MethodParameter

    @BeforeEach
    fun setUp() {
        // 기본적으로 모든 파라미터 null 반환
        `when`(webRequest.getParameter(org.mockito.ArgumentMatchers.anyString())).thenReturn(null)
    }

    // ── number 타입 시험 ────────────────────────────────────────────────────

    @Test
    fun `TOEFL_IBT 숫자값 파싱 시 올바르게 변환된다`() {
        `when`(webRequest.getParameter("TOEFL_IBT")).thenReturn("90")

        val result = resolve()

        assertThat(result["TOEFL iBT"]).isEqualTo(90.0)
    }

    @Test
    fun `IELTS 소수점 값 파싱 시 올바르게 변환된다`() {
        `when`(webRequest.getParameter("IELTS")).thenReturn("6.5")

        val result = resolve()

        assertThat(result["IELTS"]).isEqualTo(6.5)
    }

    @Test
    fun `TOEFL_IBT 범위 초과 시 BusinessException이 발생한다`() {
        `when`(webRequest.getParameter("TOEFL_IBT")).thenReturn("121")

        assertThatThrownBy { resolve() }.isInstanceOf(BusinessException::class.java)
    }

    @Test
    fun `TOEFL_IBT 문자열 입력 시 BusinessException이 발생한다`() {
        `when`(webRequest.getParameter("TOEFL_IBT")).thenReturn("abc")

        assertThatThrownBy { resolve() }.isInstanceOf(BusinessException::class.java)
    }

    // ── string enum 타입 시험: JLPT ─────────────────────────────────────────

    @Test
    fun `JLPT N1 입력 시 minScore 1점으로 변환된다`() {
        `when`(webRequest.getParameter("JLPT")).thenReturn("N1")

        val result = resolve()

        assertThat(result["JLPT"]).isEqualTo(1.0)
    }

    @Test
    fun `JLPT N2 입력 시 minScore 2점으로 변환된다`() {
        `when`(webRequest.getParameter("JLPT")).thenReturn("N2")

        val result = resolve()

        assertThat(result["JLPT"]).isEqualTo(2.0)
    }

    @Test
    fun `JLPT N5 입력 시 minScore 5점으로 변환된다`() {
        `when`(webRequest.getParameter("JLPT")).thenReturn("N5")

        val result = resolve()

        assertThat(result["JLPT"]).isEqualTo(5.0)
    }

    @Test
    fun `JLPT 유효하지 않은 값 N6 입력 시 BusinessException이 발생한다`() {
        `when`(webRequest.getParameter("JLPT")).thenReturn("N6")

        assertThatThrownBy { resolve() }.isInstanceOf(BusinessException::class.java)
    }

    @Test
    fun `JLPT 숫자 직접 입력은 더 이상 허용되지 않는다`() {
        `when`(webRequest.getParameter("JLPT")).thenReturn("2")

        assertThatThrownBy { resolve() }.isInstanceOf(BusinessException::class.java)
    }

    // ── string enum 타입 시험: HSK ──────────────────────────────────────────

    @Test
    fun `HSK 4급 입력 시 minScore 4점으로 변환된다`() {
        `when`(webRequest.getParameter("HSK")).thenReturn("4급")

        val result = resolve()

        assertThat(result["HSK"]).isEqualTo(4.0)
    }

    @Test
    fun `HSK 1급 입력 시 minScore 1점으로 변환된다`() {
        `when`(webRequest.getParameter("HSK")).thenReturn("1급")

        val result = resolve()

        assertThat(result["HSK"]).isEqualTo(1.0)
    }

    @Test
    fun `HSK 6급 입력 시 minScore 6점으로 변환된다`() {
        `when`(webRequest.getParameter("HSK")).thenReturn("6급")

        val result = resolve()

        assertThat(result["HSK"]).isEqualTo(6.0)
    }

    @Test
    fun `HSK 숫자 직접 입력은 더 이상 허용되지 않는다`() {
        `when`(webRequest.getParameter("HSK")).thenReturn("4")

        assertThatThrownBy { resolve() }.isInstanceOf(BusinessException::class.java)
    }

    @Test
    fun `HSK 7급 입력 시 BusinessException이 발생한다`() {
        `when`(webRequest.getParameter("HSK")).thenReturn("7급")

        assertThatThrownBy { resolve() }.isInstanceOf(BusinessException::class.java)
    }

    // ── string enum 타입 시험: DELF/ZD ─────────────────────────────────────

    @Test
    fun `DELF B2 입력 시 minScore 4점으로 변환된다`() {
        `when`(webRequest.getParameter("DELF")).thenReturn("B2")

        val result = resolve()

        assertThat(result["DELF"]).isEqualTo(4.0)
    }

    @Test
    fun `ZD A1 입력 시 minScore 1점으로 변환된다`() {
        `when`(webRequest.getParameter("ZD")).thenReturn("A1")

        val result = resolve()

        assertThat(result["ZD"]).isEqualTo(1.0)
    }

    @Test
    fun `DELF C3 입력 시 BusinessException이 발생한다`() {
        `when`(webRequest.getParameter("DELF")).thenReturn("C3")

        assertThatThrownBy { resolve() }.isInstanceOf(BusinessException::class.java)
    }

    // ── 복합 ────────────────────────────────────────────────────────────────

    @Test
    fun `여러 시험 동시 입력 시 모두 파싱된다`() {
        `when`(webRequest.getParameter("TOEFL_IBT")).thenReturn("90")
        `when`(webRequest.getParameter("IELTS")).thenReturn("7.0")
        `when`(webRequest.getParameter("JLPT")).thenReturn("N2")

        val result = resolve()

        assertThat(result["TOEFL iBT"]).isEqualTo(90.0)
        assertThat(result["IELTS"]).isEqualTo(7.0)
        assertThat(result["JLPT"]).isEqualTo(2.0)
    }

    @Test
    fun `파라미터가 없으면 빈 Map이 반환된다`() {
        val result = resolve()

        assertThat(result).isEmpty()
    }

    private fun resolve(): Map<String, Double> =
        @Suppress("UNCHECKED_CAST")
        resolver.resolveArgument(parameter, null, webRequest, null) as Map<String, Double>
}
