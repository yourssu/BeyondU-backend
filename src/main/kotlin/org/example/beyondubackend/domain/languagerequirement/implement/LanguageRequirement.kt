package org.example.beyondubackend.domain.languagerequirement.implement

class LanguageRequirement(
    var id: Long? = null,
    var universityId: Long? = null,
    var languageGroup: String,
    var examType: String,
    var minScore: Double,
    var levelCode: String? = null,
    var isAvailable: Boolean = true
)
