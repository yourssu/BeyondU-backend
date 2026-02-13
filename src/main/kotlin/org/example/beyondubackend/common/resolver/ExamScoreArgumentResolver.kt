package org.example.beyondubackend.common.resolver

import org.example.beyondubackend.common.annotation.ExamScoreParams
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

/**
 * @ExamScoreParams 어노테이션이 붙은 파라미터에 대해
 * QueryString에서 어학 시험 점수를 추출하여 Map으로 변환하는 ArgumentResolver
 */
class ExamScoreArgumentResolver : HandlerMethodArgumentResolver {

    companion object {
        /**
         * 지원하는 어학 시험 목록
         * 새로운 시험 추가 시 이 목록에만 추가하면 됨
         */
        private val SUPPORTED_EXAMS = mapOf(
            "TOEFL_IBT" to "TOEFL iBT",
            "TOEFL_ITP" to "TOEFL ITP",
            "IELTS" to "IELTS",
            "TOEIC" to "TOEIC",
            "TOEIC_Speaking" to "TOEIC Speaking",
            "HSK" to "HSK",
            "JLPT" to "JLPT",
            "JPT" to "JPT",
            "DELF" to "DELF",
            "TestDaF" to "TestDaF"
        )
    }

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(ExamScoreParams::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Map<String, Double> {
        return SUPPORTED_EXAMS.mapNotNull { (paramName, displayName) ->
            val value = webRequest.getParameter(paramName)?.toDoubleOrNull()
            if (value != null) displayName to value else null
        }.toMap()
    }
}
