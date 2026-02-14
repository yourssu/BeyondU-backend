package org.example.beyondubackend.domain.university.storage

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
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
    private val queryFactory: JPAQueryFactory
) : UniversityRepository {

    override fun findAllWithFilters(
        nation: String?,
        isExchange: Boolean?,
        isVisit: Boolean?,
        search: String?,
        gpa: Double?,
        nations: String?,
        major: String?,
        hasReview: Boolean?,
        examScores: Map<String, Double>,
        pageable: Pageable
    ): Page<University> {

        val query = queryFactory
            .selectFrom(universityEntity)
            .where(
                nationEq(nation),
                isExchangeEq(isExchange),
                isVisitEq(isVisit),
                searchKeyword(search),
                gpaLoe(gpa),
                nationsIn(nations),
                majorContains(major),
                hasReviewEq(hasReview),
                examScoresMatch(examScores)
            )
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())

        pageable.sort.forEach { order ->
            when (order.property) {
                "nameEng" -> if (order.isAscending) query.orderBy(universityEntity.nameEng.asc())
                            else query.orderBy(universityEntity.nameEng.desc())
                "nameKor" -> if (order.isAscending) query.orderBy(universityEntity.nameKor.asc())
                            else query.orderBy(universityEntity.nameKor.desc())
            }
        }

        val content = query.fetch().map { it.toDomain() }
        val total = queryFactory
            .select(universityEntity.count())
            .from(universityEntity)
            .where(
                nationEq(nation),
                isExchangeEq(isExchange),
                isVisitEq(isVisit),
                searchKeyword(search),
                gpaLoe(gpa),
                nationsIn(nations),
                majorContains(major),
                hasReviewEq(hasReview),
                examScoresMatch(examScores)
            )
            .fetchOne() ?: 0L

        return PageImpl(content, pageable, total)
    }

    override fun findById(id: Long): University? {
        return universityJpaRepository.findById(id).orElse(null)?.toDomain()
    }

    private fun nationEq(nation: String?): BooleanExpression? {
        return nation?.let { universityEntity.nation.eq(it) }
    }

    private fun isExchangeEq(isExchange: Boolean?): BooleanExpression? {
        return isExchange?.let { universityEntity.isExchange.eq(it) }
    }

    private fun isVisitEq(isVisit: Boolean?): BooleanExpression? {
        return isVisit?.let { universityEntity.isVisit.eq(it) }
    }

    private fun searchKeyword(search: String?): BooleanExpression? {
        return search?.let {
            universityEntity.nameKor.contains(it)
                .or(universityEntity.nameEng.contains(it))
        }
    }

    private fun gpaLoe(gpa: Double?): BooleanExpression? {
        return gpa?.let { universityEntity.minGpa.loe(it) }
    }

    private fun nationsIn(nations: String?): BooleanExpression? {
        return nations?.let {
            val nationList = it.split(",").map { nation -> nation.trim() }
            universityEntity.nation.`in`(nationList)
        }
    }

    private fun majorContains(major: String?): BooleanExpression? {
        return major?.let { universityEntity.availableMajors.contains(it) }
    }

    private fun hasReviewEq(hasReview: Boolean?): BooleanExpression? {
        return hasReview?.let { universityEntity.hasReview.eq(it) }
    }

    private fun examScoresMatch(examScores: Map<String, Double>): BooleanExpression? {
        if (examScores.isEmpty()) return null

        val examConditions = examScores.map { (examType, score) ->
            // JLPT는 숫자가 낮을수록 높은 레벨 (N1 > N2 > ... > N5)
            val scoreCondition = if (examType == "JLPT") {
                languageRequirementEntity.minScore.goe(score)
            } else {
                languageRequirementEntity.minScore.loe(score)
            }

            languageRequirementEntity.examType.eq(examType).and(scoreCondition)
        }.reduce { acc, condition -> acc.or(condition) }

        return JPAExpressions
            .selectOne()
            .from(languageRequirementEntity)
            .where(
                languageRequirementEntity.universityId.eq(universityEntity.id),
                languageRequirementEntity.isAvailable.eq(true),
                examConditions
            )
            .exists()
    }
}
