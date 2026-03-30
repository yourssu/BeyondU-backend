package org.example.beyondubackend.domain.university.storage

import jakarta.persistence.*
import org.example.beyondubackend.common.entity.BaseEntity
import org.example.beyondubackend.domain.university.implement.University

@Entity
@Table(name = "university")
class UniversityEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var semester: String,

    @Column(nullable = false)
    var region: String,

    @Column(nullable = false)
    var nation: String,

    @Column(name = "name_kor", nullable = false)
    var nameKor: String,

    @Column(name = "name_eng", nullable = false)
    var nameEng: String,

    @Column(name = "min_gpa", nullable = false)
    var minGpa: Double,

    @Column(name = "significant_note", columnDefinition = "TEXT", nullable = true)
    var significantNote: String? = null,

    @Column(columnDefinition = "TEXT", nullable = false)
    var remark: String,

    @Column(name = "location", nullable = true)
    var location: String? = null,

    @Column(name = "student_count", nullable = true)
    var studentCount: String? = null,

    @Column(columnDefinition = "TEXT", name = "available_major", nullable = true)
    var availableMajor: String? = null,

    @Column(columnDefinition = "TEXT", name = "available_subject", nullable = true)
    var availableSubject: String? = null,

    @Column(name = "website_url", columnDefinition = "TEXT", nullable = true)
    var websiteUrl: String? = null,

    @Column(name = "is_exchange", nullable = false)
    var isExchange: Boolean,

    @Column(name = "is_visit", nullable = false)
    var isVisit: Boolean,

    @Column(nullable = true)
    var badge: String? = null,

    @Column(name = "has_review", nullable = false)
    var hasReview: Boolean = false,

    @Column(name = "review_year", nullable = true)
    var reviewYear: String? = null

) : BaseEntity() {
    companion object {
        fun from(university: University) = UniversityEntity(
            id = university.id,
            semester = university.semester,
            region = university.region,
            nation = university.nation,
            nameKor = university.nameKor,
            nameEng = university.nameEng,
            minGpa = university.minGpa,
            significantNote = university.significantNote,
            remark = university.remark,
            location = university.location,
            studentCount = university.studentCount,
            availableMajor = university.availableMajor,
            availableSubject = university.availableSubject,
            websiteUrl = university.websiteUrl,
            isExchange = university.isExchange,
            isVisit = university.isVisit,
            badge = university.badge,
            hasReview = university.hasReview,
            reviewYear = university.reviewYear
        )
    }

    fun toDomain(): University {
        return University(
            id = id,
            semester = semester,
            region = region,
            nation = nation,
            nameKor = nameKor,
            nameEng = nameEng,
            minGpa = minGpa,
            significantNote = significantNote,
            remark = remark,
            location = location,
            studentCount = studentCount,
            availableMajor = availableMajor,
            availableSubject = availableSubject,
            websiteUrl = websiteUrl,
            isExchange = isExchange,
            isVisit = isVisit,
            badge = badge ?: "",
            hasReview = hasReview,
            reviewYear = reviewYear
        )
    }
}
