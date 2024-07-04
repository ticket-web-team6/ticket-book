package com.team6.ticketbook.domain.show.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import com.team6.ticketbook.domain.show.dto.ShowSearchFilter
import com.team6.ticketbook.domain.show.model.QShow
import com.team6.ticketbook.domain.show.model.Show
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface ShowRepository : JpaRepository<Show, Long>, ShowQueryDslRepository {
}

interface ShowQueryDslRepository {
    fun findAllPaginated(pageable: Pageable): Page<Show>
    fun findByFilterPaginated(filter: ShowSearchFilter, pageable: Pageable): Page<Show>
}

class ShowQueryDslRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : ShowQueryDslRepository {
    private val show = QShow.show

    override fun findAllPaginated(pageable: Pageable): Page<Show> {
        val totalCount = jpaQueryFactory
            .select(show.count())
            .from(show)
            .fetchOne() ?: 0L

        val query = jpaQueryFactory
            .selectFrom(show)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())

        val results = query.fetch()
        return PageImpl(results, pageable, totalCount)
    }

    override fun findByFilterPaginated(filter: ShowSearchFilter, pageable: Pageable): Page<Show> {
        val totalCount = jpaQueryFactory
            .select(show.count())
            .from(show)
            .where(
                titleLike(filter.title),
                categoryEq(filter.category),
                dateBetween(filter.date)
            )
            .fetchOne() ?: 0L

        val query = jpaQueryFactory
            .selectFrom(show)
            .where(
                titleLike(filter.title),
                categoryEq(filter.category),
                dateBetween(filter.date)
            )
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())

        val results = query.fetch()
        return PageImpl(results, pageable, totalCount)
    }

    private fun titleLike(title: String?): BooleanExpression? {
        return if (title.isNullOrEmpty()) null else show.title.contains(title)
    }

    private fun categoryEq(category: String?): BooleanExpression? {
        return if (category.isNullOrEmpty()) null else show.category.eq(category)
    }

    private fun dateBetween(date: LocalDate?): BooleanExpression? {
        return if (date == null) null else show.startDate.before(date).and(show.endDate.after(date))
    }

}
