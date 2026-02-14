package org.example.beyondubackend.common.annotation

/**
 * 어학 시험 점수 파라미터를 자동으로 파싱하여 Map으로 주입하는 어노테이션
 *
 * 사용 예시:
 * ```
 * fun getUniversities(
 *     @ExamScoreParams examScores: Map<String, Double>
 * )
 * ```
 *
 * QueryString: ?TOEFL_IBT=90&IELTS=6.5&HSK=4
 * 결과: {"TOEFL iBT": 90.0, "IELTS": 6.5, "HSK": 4.0}
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class ExamScoreParams
