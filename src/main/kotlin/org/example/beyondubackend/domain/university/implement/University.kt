package org.example.beyondubackend.domain.university.implement

class University(
    var id: Long? = null,
    var serialNumber: String,
    var region: String,
    var nation: String,
    var nameKor: String,
    var nameEng: String,
    var minGpa: Double = 0.0,
    var isExchange: Boolean,
    var isVisiting: Boolean,
    var availableMajors: String,
    var note: String,
    var thumbnailUrl: String? = null,
    var websiteUrl: String? = null,
    var availableSemester: String? = null,
)
