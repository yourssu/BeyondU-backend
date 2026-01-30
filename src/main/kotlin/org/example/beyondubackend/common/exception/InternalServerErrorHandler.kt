package org.example.beyondubackend.common.exception

import org.example.beyondubackend.common.code.ErrorCode
import org.example.beyondubackend.common.dto.ApiResponse
import org.springframework.dao.DataAccessResourceFailureException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice
class ServerExceptionHandler {

    // 데이터베이스 연결 실패 등 인프라 오류
    @ExceptionHandler(DataAccessResourceFailureException::class)
    fun handleDatabaseException(e: DataAccessResourceFailureException): ResponseEntity<ApiResponse<Unit>> {
        return ApiResponse.fail(ErrorCode.DATABASE_ERROR)
    }

    // 그 외 모든 서버 에러
    @ExceptionHandler(Exception::class)
    fun handleAllException(e: Exception): ResponseEntity<ApiResponse<Unit>> {
        return ApiResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR)
    }
}
