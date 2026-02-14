package org.example.beyondubackend.domain.languagerequirement.storage

import jakarta.persistence.*
import org.example.beyondubackend.common.entity.BaseEntity
import org.example.beyondubackend.domain.languagerequirement.implement.LanguageRequirement

@Entity
@Table(name = "language_requirement")
class LanguageRequirementEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "university_id", nullable = false)
    var universityId: Long,

    @Column(name = "language_group", nullable = false)
    var languageGroup: String,

    @Column(name = "exam_type", nullable = false)
    var examType: String,

    @Column(name = "min_score", nullable = false)
    var minScore: Double,

    @Column(name = "level_code")
    var levelCode: String? = null,

    @Column(name = "is_available", nullable = false)
    var isAvailable: Boolean = true
) : BaseEntity() {
    companion object {
        fun from(languageRequirement: LanguageRequirement) =
            LanguageRequirementEntity(
                id = languageRequirement.id,
                universityId = languageRequirement.universityId!!,
                languageGroup = languageRequirement.languageGroup,
                examType = languageRequirement.examType,
                minScore = languageRequirement.minScore,
                levelCode = languageRequirement.levelCode,
                isAvailable = languageRequirement.isAvailable
            )
    }

    fun toDomain(): LanguageRequirement {
        return LanguageRequirement(
            id = id,
            universityId = universityId,
            languageGroup = languageGroup,
            examType = examType,
            minScore = minScore,
            levelCode = levelCode,
            isAvailable = isAvailable
        )
    }
}
