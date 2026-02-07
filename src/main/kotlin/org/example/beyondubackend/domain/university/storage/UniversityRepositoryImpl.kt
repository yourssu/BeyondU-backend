package org.example.beyondubackend.domain.university.storage

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
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
        pageable: Pageable
    ): Page<University> {

        val query = queryFactory
            .selectFrom(universityEntity)
            .where(
                nationEq(nation),
                isExchangeEq(isExchange),
                isVisitEq(isVisit),
                searchKeyword(search)
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
                searchKeyword(search)
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
}
