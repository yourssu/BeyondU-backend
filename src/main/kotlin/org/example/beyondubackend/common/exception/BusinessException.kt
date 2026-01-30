package org.example.beyondubackend.common.exception

import org.example.beyondubackend.common.code.ErrorCode

open class BusinessException(
    val errorCode: ErrorCode,
    message: String = errorCode.message
) : RuntimeException(message)
