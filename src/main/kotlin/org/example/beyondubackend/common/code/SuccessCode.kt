
package org.example.beyondubackend.common.code

import org.springframework.http.HttpStatus

enum class SuccessCode(val code: String, val httpStatus: HttpStatus, val message: String) {
    OK("S001", HttpStatus.OK, "요청 성공"),
    CREATED("S002", HttpStatus.CREATED, "생성 성공");
}
