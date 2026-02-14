package org.example.beyondubackend.domain.university.business.dto

import org.example.beyondubackend.domain.university.implement.University

data class UniversityDetailResponse(
    val id: Long,
    val nameKor: String,
    val nameEng: String,
    val nation: String,
    val region: String,
    val isExchange: Boolean,
    val isVisit: Boolean,
    val badge: String,
    val hasReview: Boolean,
    val minGpa: Double,
    val websiteUrl: String?,
    val significantNote: String?,
    val remark: String?,
    val languageRequirements: List<LanguageRequirementResponse>,
    val availableMajors: List<String>?,
    val courseListUrl: String?,
    val studentCount: String?,
) {
    companion object {
        fun from(
            university: University,
            languageRequirements: List<LanguageRequirementResponse>
        ): UniversityDetailResponse {
            return UniversityDetailResponse(
                id = university.id!!,
                nameKor = university.nameKor,
                nameEng = university.nameEng,
                nation = university.nation,
                region = university.region,
                isExchange = university.isExchange,
                isVisit = university.isVisit,
                badge = university.badge,
                hasReview = university.hasReview,
                minGpa = university.minGpa,
                websiteUrl = university.websiteUrl,
                significantNote = university.significantNote,
                remark = university.remark,
                languageRequirements = languageRequirements,
                availableMajors = parseAvailableMajors(university.availableMajors),
                courseListUrl = parseCourseListUrl(university.availableMajors),
                studentCount = parseStudentCount(university.remark),
            )
        }

        /**
         * availableMajors에서 수학 가능 학과 리스트 파싱
         * 예: "★ 수학가능학과: Architecture, Arts and Sciences, ..." -> ["Architecture", "Arts and Sciences", ...]
         */
        private fun parseAvailableMajors(availableMajors: String?): List<String>? {
            if (availableMajors == null) return null
            val regex = """★\s*수학가능학과:\s*([^★]+)""".toRegex()
            val matchResult = regex.find(availableMajors) ?: return null
            val majorsText = matchResult.groupValues[1].trim()

            return majorsText
                .split(",")
                .map { it.trim() }
                .filter { it.isNotBlank() }
                .takeIf { it.isNotEmpty() }
        }

        /**
         * availableMajors에서 수강가능과목 URL 파싱
         * 예: "★ 수강가능과목(2025): https://..." -> "https://..."
         */
        private fun parseCourseListUrl(availableMajors: String?): String? {
            if (availableMajors == null) return null
            val regex = """★\s*수강가능과목[^:]*:\s*(https?://\S+)""".toRegex()
            return regex.find(availableMajors)?.groupValues?.get(1)?.trim()
        }

        /**
         * remark에서 학생 수 파싱
         * 예: "... 학생 수 약 28,600명 ..." -> "약 28,600명"
         * 없으면 "학생 수 정보 없음" 반환
         */
        private fun parseStudentCount(remark: String?): String {
            if (remark == null) return "학생 수 정보 없음"
            val regex = """학생\s*수\s*약?\s*([\d,]+)\s*명""".toRegex()
            val count = regex.find(remark)?.groupValues?.get(1)?.trim()
            return if (count != null) "약 ${count} 명" else "학생 수 정보 없음"
        }
    }
}
