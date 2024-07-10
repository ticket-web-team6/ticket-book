package com.team6.ticketbook.domain.show.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import com.team6.ticketbook.domain.show.model.QShow
import com.team6.ticketbook.domain.show.model.Show
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ShowRepository : JpaRepository<Show, Long>, ShowQueryDslRepository

interface ShowQueryDslRepository {
    fun findAllPaginated(pageable: Pageable): Page<Show>
    fun findByTitlePaginated(title: String, pageable: Pageable): Page<Show>
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

    override fun findByTitlePaginated(title: String, pageable: Pageable): Page<Show> {
        val totalCount = jpaQueryFactory
            .select(show.count())
            .from(show)
            .where(titleLike(title))
            .fetchOne() ?: 0L

        val query = jpaQueryFactory
            .selectFrom(show)
            .where(titleLike(title))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())

        val results = query.fetch()
        return PageImpl(results, pageable, totalCount)
    }

    private fun titleLike(title: String?): BooleanExpression? {
        return if (title.isNullOrEmpty()) null else show.title.contains(title)
    }
}
