package com.team6.ticketbook.domain.show.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import com.team6.ticketbook.domain.show.dto.ShowResponse
import com.team6.ticketbook.domain.show.dto.TopSearchKeyword
import com.team6.ticketbook.domain.show.model.QShow
import com.team6.ticketbook.domain.show.model.Show
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ZSetOperations
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

@Repository
interface ShowRepository : JpaRepository<Show, Long>, ShowQueryDslRepository, ShowRedisRepository

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

interface ShowRedisRepository {
    fun getTopSearchList(limit: Long): Set<TopSearchKeyword>
    fun saveTermTopSearch(term: String)
    fun saveSearchDataWithTerm(term: String, showInfos: Page<ShowResponse>)
    fun getSearchDataWithTerm(term: String): Page<ShowResponse>?
}

class ShowRedisRepositoryImpl(
    private val redisTemplate: RedisTemplate<String, String>,
    private val redisTemplateForShowResponse: RedisTemplate<String, Page<ShowResponse>>,
) : ShowRedisRepository {
    private val topSearchZSet: ZSetOperations<String, String> = redisTemplate.opsForZSet()
    private val searchV2Hash: HashOperations<String, String, Page<ShowResponse>> =
        redisTemplateForShowResponse.opsForHash()

    override fun getTopSearchList(limit: Long): Set<TopSearchKeyword> {
        val rangeWithScores = topSearchZSet.reverseRangeWithScores("topSearch", 0, limit - 1)
        if (rangeWithScores != null) {
            return rangeWithScores.map { TopSearchKeyword(it.value, it.score) }.toSet()
        }
        return emptySet()
    }

    override fun saveTermTopSearch(term: String) {
        topSearchZSet.incrementScore("topSearch", term, 1.0)
        redisTemplate.expire(term, 10, TimeUnit.MINUTES)
    }

    override fun saveSearchDataWithTerm(term: String, showInfos: Page<ShowResponse>) {
        searchV2Hash.put("searchList", term, showInfos)
        redisTemplateForShowResponse.expire(term, 10, TimeUnit.MINUTES)
    }

    override fun getSearchDataWithTerm(term: String): Page<ShowResponse>? {
        return searchV2Hash.get("searchList", term)

    }

}
