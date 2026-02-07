package org.example.beyondubackend.common.dto

data class PageInfo(
    val currentPage: Int,
    val totalElements: Long,
    val totalPages: Int,
    val isLast: Boolean
)
