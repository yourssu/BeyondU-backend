package org.example.beyondubackend.domain.university.application.dto

import org.example.beyondubackend.domain.university.business.query.UniversityQuery

data class UniversitySearchRequest(
    val nation: String? = null,
    val isExchange: Boolean? = null,
    val isVisit: Boolean? = null,
    val search: String? = null
) {
    fun toQuery(): UniversityQuery {
        return UniversityQuery(
            nation = nation,
            isExchange = isExchange,
            isVisit = isVisit,
            search = search
        )
    }
}
