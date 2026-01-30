package org.example.beyondubackend.common.exception

import org.example.beyondubackend.common.code.ErrorCode
import org.example.beyondubackend.common.dto.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.resource.NoResourceFoundException


@RestControllerAdvice
class ClientExceptionHandler {

    // @Valid 유효성 검사 실패
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(e: MethodArgumentNotValidException): ResponseEntity<ApiResponse<Unit>> {
        val errorMessage = e.bindingResult.fieldErrors.firstOrNull()?.defaultMessage
        return ApiResponse.fail(ErrorCode.INVALID_INPUT, errorMessage)
    }

    // 404: 존재하지 않는 API 경로
    @ExceptionHandler(NoResourceFoundException::class)
    fun handleNoResource(e: NoResourceFoundException): ResponseEntity<ApiResponse<Unit>> {
        return ApiResponse.fail(ErrorCode.ENDPOINT_NOT_FOUND)
    }

    // 405: 지원하지 않는 HTTP 메서드 호출
    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleMethodNotSupported(e: HttpRequestMethodNotSupportedException): ResponseEntity<ApiResponse<Unit>> {
        return ApiResponse.fail(ErrorCode.METHOD_NOT_ALLOWED)
    }

    // 타입 불일치 (Path Variable이나 Query Param 타입 오류)
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleTypeMismatch(e: MethodArgumentTypeMismatchException): ResponseEntity<ApiResponse<Unit>> {
        return ApiResponse.fail(ErrorCode.BAD_REQUEST, "파라미터 타입이 올바르지 않습니다.")
    }

    // JSON 파싱 에러 (Body 데이터 형식 오류)
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleJsonError(e: HttpMessageNotReadableException): ResponseEntity<ApiResponse<Unit>> {
        return ApiResponse.fail(ErrorCode.BAD_REQUEST, "잘못된 JSON 형식입니다.")
    }
}
