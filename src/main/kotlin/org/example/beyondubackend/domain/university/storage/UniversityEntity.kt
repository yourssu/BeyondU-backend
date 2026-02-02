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

    @Column(name = "significant_note", columnDefinition = "TEXT", nullable = false)
    var significantNote: String,

    @Column(columnDefinition = "TEXT", nullable = false)
    var remark: String,

    @Column(columnDefinition = "TEXT", name = "available_majors", nullable = true)
    var availableMajors: String? = null,

    @Column(name="website_url", nullable = true)
    var websiteUrl: String? = null,

    @Column(name="thumbnail_url", nullable = true)
    var thumbnailUrl: String? = null,

    @Column(name="available_semester", nullable = true)
    var availableSemester: String? = null,

    @Column(name = "is_exchange", nullable = false)
    var isExchange: Boolean,

    @Column(name = "is_visit", nullable = false)
    var isVisit: Boolean,
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
            availableMajors = university.availableMajors,
            websiteUrl = university.websiteUrl,
            thumbnailUrl = university.thumbnailUrl,
            availableSemester = university.availableSemester,
            isExchange = university.isExchange,
            isVisit = university.isVisit
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
            availableMajors = availableMajors,
            websiteUrl = websiteUrl,
            thumbnailUrl = thumbnailUrl,
            availableSemester = availableSemester,
            isExchange = isExchange,
            isVisit = isVisit
        )
    }
}
