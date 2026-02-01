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
    var serialNumber: String,

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

    @Column(name = "is_exchange", nullable = false)
    var isExchange: Boolean,

    @Column(name = "is_visiting", nullable = false)
    var isVisiting: Boolean,

    @Column(columnDefinition = "TEXT", name = "available_majors", nullable = false)
    var availableMajors: String,

    @Column(columnDefinition = "TEXT", nullable = false)
    var note: String,

    // TO DO: 기획 확정 후 null 여부 결정
    @Column(name="thumbnail_url", nullable = true)
    var thumbnailUrl: String? = null,

    @Column(name="website_url", nullable = true)
    var websiteUrl: String? = null,

    @Column(nullable = true)
    var availableSemester: String? = null,
) : BaseEntity() {
    companion object {
        fun from(university: University) = UniversityEntity(

            id = university.id,
            serialNumber = university.serialNumber,
            region = university.region,
            nation = university.nation,
            nameKor = university.nameKor,
            nameEng = university.nameEng,
            minGpa = university.minGpa,
            isExchange = university.isExchange,
            isVisiting = university.isVisiting,
            availableMajors = university.availableMajors,
            note = university.note,
            thumbnailUrl = university.thumbnailUrl,
            websiteUrl = university.websiteUrl,
            availableSemester = university.availableSemester,
        )
    }

    fun toDomain(): University {
        return University(
            id = id,
            serialNumber = serialNumber,
            region = region,
            nation = nation,
            nameKor = nameKor,
            nameEng = nameEng,
            minGpa = minGpa,
            isExchange = isExchange,
            isVisiting = isVisiting,
            availableMajors = availableMajors,
            note = note,
            thumbnailUrl = thumbnailUrl,
            websiteUrl = websiteUrl,
            availableSemester = availableSemester,
        )
    }
}
