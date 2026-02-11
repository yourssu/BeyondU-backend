package org.example.beyondubackend.domain.university.implement

class University(
    var id: Long? = null,
    var semester: String,
    var region: String,
    var nation: String,
    var nameKor: String,
    var nameEng: String,
    var minGpa: Double,
    var significantNote: String,
    var remark: String,
    var availableMajors: String? = null,
    var websiteUrl: String? = null,
    var thumbnailUrl: String? = null,
    var availableSemester: String? = null,
    var isExchange: Boolean,
    var isVisit: Boolean,
    var reviewSummary: String? = null,
    var rating: String? = null,
    var cost: Long? = null
)
