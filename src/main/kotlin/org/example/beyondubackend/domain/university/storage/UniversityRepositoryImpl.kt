package org.example.beyondubackend.domain.university.storage

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.example.beyondubackend.common.enums.ExamType
import org.example.beyondubackend.common.enums.SubMajor
import org.example.beyondubackend.domain.languagerequirement.storage.QLanguageRequirementEntity.languageRequirementEntity
import org.example.beyondubackend.domain.university.implement.University
import org.example.beyondubackend.domain.university.implement.UniversityRepository
import org.example.beyondubackend.domain.university.storage.QUniversityEntity.universityEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class UniversityRepositoryImpl(
    private val universityJpaRepository: UniversityJpaRepository,
    private val queryFactory: JPAQueryFactory,
) : UniversityRepository {
    override fun findAllWithFilters(
        nations: List<String>?,
        regions: List<String>?,
        languageGroups: List<String>?,
        isExchange: Boolean?,
        isVisit: Boolean?,
        search: String?,
        gpa: Double?,
        major: String?,
        majors: List<String>?,
        hasReview: Boolean?,
        examScores: Map<String, Double>,
        pageable: Pageable,
    ): Page<University> {
        val query =
            queryFactory
                .selectFrom(universityEntity)
                .where(
                    nationsIn(nations),
                    regionsIn(regions),
                    languageGroupsIn(languageGroups),
                    isExchangeEq(isExchange),
                    isVisitEq(isVisit),
                    searchKeyword(search),
                    gpaLoe(gpa),
                    majorContains(major),
                    majorsContains(majors),
                    hasReviewEq(hasReview),
                    examScoresMatch(examScores),
                ).offset(pageable.offset)
                .limit(pageable.pageSize.toLong())

        pageable.sort.forEach { order ->
            when (order.property) {
                "nameEng" ->
                    if (order.isAscending) {
                        query.orderBy(universityEntity.nameEng.asc())
                    } else {
                        query.orderBy(universityEntity.nameEng.desc())
                    }
                "nameKor" ->
                    if (order.isAscending) {
                        query.orderBy(universityEntity.nameKor.asc())
                    } else {
                        query.orderBy(universityEntity.nameKor.desc())
                    }
            }
        }

        val content = query.fetch().map { it.toDomain() }
        val total =
            queryFactory
                .select(universityEntity.count())
                .from(universityEntity)
                .where(
                    nationsIn(nations),
                    regionsIn(regions),
                    languageGroupsIn(languageGroups),
                    isExchangeEq(isExchange),
                    isVisitEq(isVisit),
                    searchKeyword(search),
                    gpaLoe(gpa),
                    majorContains(major),
                    majorsContains(majors),
                    hasReviewEq(hasReview),
                    examScoresMatch(examScores),
                ).fetchOne() ?: 0L

        return PageImpl(content, pageable, total)
    }

    override fun findById(id: Long): University? = universityJpaRepository.findById(id).orElse(null)?.toDomain()

    private fun nationsIn(nations: List<String>?): BooleanExpression? =
        nations
            ?.takeIf {
                it.isNotEmpty()
            }?.let { universityEntity.nation.`in`(it) }

    private fun regionsIn(regions: List<String>?): BooleanExpression? =
        regions
            ?.takeIf { it.isNotEmpty() }
            ?.let { universityEntity.region.`in`(it) }

    private fun languageGroupsIn(languageGroups: List<String>?): BooleanExpression? {
        if (languageGroups.isNullOrEmpty()) return null
        return JPAExpressions
            .selectOne()
            .from(languageRequirementEntity)
            .where(
                languageRequirementEntity.universityId.eq(universityEntity.id),
                languageRequirementEntity.languageGroup.`in`(languageGroups),
            ).exists()
    }

    private fun isExchangeEq(isExchange: Boolean?): BooleanExpression? = isExchange?.let { universityEntity.isExchange.eq(it) }

    private fun isVisitEq(isVisit: Boolean?): BooleanExpression? = isVisit?.let { universityEntity.isVisit.eq(it) }

    private fun searchKeyword(search: String?): BooleanExpression? =
        search?.let {
            universityEntity.nameKor
                .contains(it)
                .or(universityEntity.nameEng.contains(it))
        }

    private fun gpaLoe(gpa: Double?): BooleanExpression? = gpa?.let { universityEntity.minGpa.loe(it) }

    private fun majorContains(major: String?): BooleanExpression? = major?.let { universityEntity.availableMajor.contains(it) }

    private fun majorsContains(majors: List<String>?): BooleanExpression? {
        if (majors.isNullOrEmpty()) return null
        val keywords = majors
            .mapNotNull { name -> SubMajor.entries.find { it.displayName == name } }
            .flatMap { it.dbKeywords }
            .distinct()
        if (keywords.isEmpty()) return null
        return keywords
            .map { universityEntity.availableMajor.contains(it) }
            .reduce { acc, expr -> acc.or(expr) }
    }

    private fun hasReviewEq(hasReview: Boolean?): BooleanExpression? = hasReview?.let { universityEntity.hasReview.eq(it) }

    private fun examScoresMatch(examScores: Map<String, Double>): BooleanExpression? {
        if (examScores.isEmpty()) return null

        val examConditions =
            examScores
                .map { (examType, score) ->
                    // JLPT는 숫자가 낮을수록 높은 레벨 (N1 > N2 > ... > N5)
                    val scoreCondition =
                        if (examType == ExamType.JLPT.displayName) {
                            languageRequirementEntity.minScore.goe(score)
                        } else {
                            languageRequirementEntity.minScore.loe(score)
                        }

                    languageRequirementEntity.examType.eq(examType).and(scoreCondition)
                }.reduce { acc, condition -> acc.or(condition) }

        // 조건을 만족하는 language_requirement가 존재하는 경우
        val hasMatchingRequirement =
            JPAExpressions
                .selectOne()
                .from(languageRequirementEntity)
                .where(
                    languageRequirementEntity.universityId.eq(universityEntity.id),
                    examConditions,
                ).exists()

        // language_requirement 자체가 없는 경우 (어학 요구사항 없음)
        val hasNoRequirement =
            JPAExpressions
                .selectOne()
                .from(languageRequirementEntity)
                .where(
                    languageRequirementEntity.universityId.eq(universityEntity.id),
                ).notExists()

        // 둘 중 하나라도 true면 조회 가능
        return hasMatchingRequirement.or(hasNoRequirement)
    }
}
