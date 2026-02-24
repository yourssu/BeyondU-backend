package org.example.beyondubackend.common.code

import org.springframework.http.HttpStatus

enum class ErrorCode(val code: String, val httpStatus: HttpStatus, val message: String) {
    // COMMON
    INVALID_INPUT("C400_001", HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다."),
    BAD_REQUEST("C400_002", HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    NOT_FOUND("C404_001", HttpStatus.NOT_FOUND, "존재하지 않는 리소스입니다."),
    ENDPOINT_NOT_FOUND("C404_002", HttpStatus.NOT_FOUND, "존재하지 않는 엔드포인트입니다."),
    METHOD_NOT_ALLOWED("C405_001", HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP 메서드입니다."),

    // SERVER
    INTERNAL_SERVER_ERROR("S500_001", HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),
    DATABASE_ERROR("S503_001", HttpStatus.SERVICE_UNAVAILABLE, "데이터베이스 통신 오류가 발생했습니다."),

    // UNIVERSITY
    UNIVERSITY_NOT_FOUND("U404_001", HttpStatus.NOT_FOUND, "대학교를 찾을 수 없습니다."),

    // LANGUAGE REQUIREMENT
    INVALID_EXAM_SCORE("L400_001", HttpStatus.BAD_REQUEST, "어학 점수가 유효 범위를 벗어났습니다.")
}
