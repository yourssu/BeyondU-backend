package org.example.beyondubackend.common.dto

import com.fasterxml.jackson.annotation.JsonInclude
import org.example.beyondubackend.common.code.ErrorCode
import org.example.beyondubackend.common.code.SuccessCode
import org.springframework.http.ResponseEntity

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiResponse<T>(
    val code: String,
    val message: String,
    val result: T? = null
) {
    companion object {
        fun <T> success(result: T): ResponseEntity<ApiResponse<T>> =
            ResponseEntity.ok(ApiResponse(
                code = SuccessCode.OK.code,
                message = SuccessCode.OK.message,
                result = result
            ))

        fun <T> success(successCode: SuccessCode, result: T? = null): ResponseEntity<ApiResponse<T>> =
            ResponseEntity.status(successCode.httpStatus)
                .body(ApiResponse(successCode.code, successCode.message, result))

        fun fail(errorCode: ErrorCode, message: String? = null): ResponseEntity<ApiResponse<Unit>> =
            ResponseEntity.status(errorCode.httpStatus)
                .body(ApiResponse(
                    code = errorCode.code,
                    message = message ?: errorCode.message
                ))
    }
}
