package org.example.beyondubackend.common.resolver

import org.example.beyondubackend.common.annotation.ExamScoreParams
import org.example.beyondubackend.common.code.ErrorCode
import org.example.beyondubackend.common.exception.BusinessException
import org.example.beyondubackend.domain.languagerequirement.implement.ExamType
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

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(ExamScoreParams::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Map<String, Double> {
        return ExamType.entries.mapNotNull { examType ->
            val value = webRequest.getParameter(examType.paramName)?.toDoubleOrNull()
            if (value != null) {
                if (value < examType.minScore || value > examType.maxScore) {
                    throw BusinessException(
                        ErrorCode.INVALID_EXAM_SCORE,
                        "${examType.displayName} 점수는 ${examType.minScore} ~ ${examType.maxScore} 범위여야 합니다."
                    )
                }
                examType.displayName to value
            } else null
        }.toMap()
    }
}
