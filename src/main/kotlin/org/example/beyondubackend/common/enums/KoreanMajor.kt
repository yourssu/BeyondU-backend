package org.example.beyondubackend.common.enums

import org.example.beyondubackend.common.enums.SubMajor.*

enum class KoreanMajor(
    val displayName: String,
    val subMajors: List<SubMajor>,
) {
    // 경영/경제 분야
    경영학부("경영학부", listOf(BUSINESS_ADMIN, MANAGEMENT)),
    벤처중소기업학과("벤처중소기업학과", listOf(BUSINESS_ADMIN)),
    혁신경영학과("혁신경영학과", listOf(BUSINESS_ADMIN)),
    회계학과("회계학과", listOf(ACCOUNTING)),
    금융학부("금융학부", listOf(ACCOUNTING)),
    회계세무학과("회계세무학과", listOf(ACCOUNTING)),
    경제학과("경제학과", listOf(ECONOMICS)),
    금융경제학과("금융경제학과", listOf(ECONOMICS)),
    국제무역학과("국제무역학과", listOf(ECONOMICS, INTERNATIONAL_BUSINESS)),
    글로벌통상학과("글로벌통상학과", listOf(INTERNATIONAL_BUSINESS, INTERNATIONAL_STUDIES)),
    벤처경영학과("벤처경영학과", listOf(MANAGEMENT)),
    미디어경영학과("미디어경영학과", listOf(MANAGEMENT)),

    // IT/공학 분야
    컴퓨터학부("컴퓨터학부", listOf(COMPUTER_SCIENCE)),
    소프트웨어학부("소프트웨어학부", listOf(COMPUTER_SCIENCE)),
    AI융합학부("AI융합학부", listOf(COMPUTER_SCIENCE)),
    AI소프트웨어학과("AI소프트웨어학과", listOf(COMPUTER_SCIENCE)),
    전자정보공학부_IT융합전공("전자정보공학부 IT융합전공", listOf(INFORMATION_TECHNOLOGY)),
    정보사회학과("정보사회학과", listOf(INFORMATION_TECHNOLOGY, SOCIAL_SCIENCES)),
    정보보호학과("정보보호학과", listOf(INFORMATION_TECHNOLOGY)),
    전기공학부("전기공학부", listOf(ELECTRICAL_ENGINEERING)),
    전자정보공학부_전자공학전공("전자정보공학부 전자공학전공", listOf(ELECTRICAL_ENGINEERING)),
    기계공학부("기계공학부", listOf(MECHANICAL_ENGINEERING)),
    산업_정보시스템공학과("산업·정보시스템공학과", listOf(INDUSTRIAL_ENGINEERING)),
    건축학부("건축학부", listOf(ARCHITECTURE)),

    // 인문/사회/법학 분야
    법학과("법학과", listOf(LAW)),
    국제법무학과("국제법무학과", listOf(LAW, INTERNATIONAL_STUDIES)),
    언론홍보학과("언론홍보학과", listOf(COMMUNICATION)),
    글로벌미디어학부("글로벌미디어학부", listOf(COMMUNICATION, DESIGN)),
    정치외교학과("정치외교학과", listOf(INTERNATIONAL_STUDIES)),
    사학과("사학과", listOf(HISTORY)),
    기독교학과("기독교학과", listOf(THEOLOGY)),
    국어국문학과("국어국문학과", listOf(LANGUAGES)),
    영어영문학과("영어영문학과", listOf(LANGUAGES)),
    독어독문학과("독어독문학과", listOf(LANGUAGES)),
    불어불문학과("불어불문학과", listOf(LANGUAGES)),
    중어중문학과("중어중문학과", listOf(LANGUAGES)),
    일어일문학과("일어일문학과", listOf(LANGUAGES)),
    사회복지학부("사회복지학부", listOf(SOCIAL_SCIENCES, PSYCHOLOGY)),
    행정학부("행정학부", listOf(SOCIAL_SCIENCES)),
    평생교육학과("평생교육학과", listOf(PSYCHOLOGY, EDUCATION)),

    // 자연과학/예술/기타 분야
    수학과("수학과", listOf(MATHEMATICS_SCIENCE)),
    물리학과("물리학과", listOf(MATHEMATICS_SCIENCE)),
    화학과("화학과", listOf(MATHEMATICS_SCIENCE, LIFE_SCIENCES)),
    정보통계_보험수리학과("정보통계·보험수리학과", listOf(MATHEMATICS_SCIENCE)),
    의생명시스템학부("의생명시스템학부", listOf(LIFE_SCIENCES)),
    예술창작학부("예술창작학부", listOf(DESIGN)),
}
