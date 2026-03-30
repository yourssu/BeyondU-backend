package org.example.beyondubackend.common.exception

import org.example.beyondubackend.common.code.ErrorCode
import org.example.beyondubackend.common.dto.ApiResponse
import org.slf4j.LoggerFactory
import org.springframework.dao.DataAccessResourceFailureException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice
class ServerExceptionHandler {

    private val logger = LoggerFactory.getLogger(ServerExceptionHandler::class.java)

    // 데이터베이스 연결 실패 등 인프라 오류
    @ExceptionHandler(DataAccessResourceFailureException::class)
    fun handleDatabaseException(e: DataAccessResourceFailureException): ResponseEntity<ApiResponse<Unit>> {
        logger.error("Database access failure", e)
        return ApiResponse.fail(ErrorCode.DATABASE_ERROR, e.message)
    }

    // 그 외 모든 서버 에러
    @ExceptionHandler(Exception::class)
    fun handleAllException(e: Exception): ResponseEntity<ApiResponse<Unit>> {
        logger.error("Unhandled exception", e)
        return ApiResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR, e.message)
    }
}
