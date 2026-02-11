package org.example.beyondubackend.domain.university.business.query

data class UniversityQuery(
    val nation: String? = null,
    val isExchange: Boolean? = null,
    val isVisit: Boolean? = null,
    val search: String? = null
)
