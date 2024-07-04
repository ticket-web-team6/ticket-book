package com.team6.ticketbook.domain.show.service

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import com.team6.ticketbook.domain.show.dto.CreateShowRequest
import com.team6.ticketbook.domain.show.dto.ShowResponse
import com.team6.ticketbook.domain.show.dto.UpdateShowImageRequest
import com.team6.ticketbook.domain.show.model.QShow
import com.team6.ticketbook.domain.show.model.Show
import com.team6.ticketbook.domain.show.repository.ShowRepository
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ShowService(
    private val showRepository: ShowRepository,
    private val entityManager: EntityManager,
) {
    fun getShowById(showId: Long): ShowResponse {
        val show = showRepository.findByIdOrNull(showId) ?: throw RuntimeException()
        return ShowResponse.from(show)
    }

    @Transactional
    fun createShow(request: CreateShowRequest): ShowResponse? {
        val show = Show(
            title = request.title,
            vipPrice = request.vipPrice,
            rPrice = request.rPrice,
            sPrice = request.sPrice,
            aPrice = request.aPrice,
            startDate = request.startDate,
            endDate = request.endDate,
            startTime = request.startTime,
            mainImageUrl = request.mainImageUrl,
            info = request.info,
            category = request.category,
        )
        // Todo : Seat 생성 로직 구현 필요
        return showRepository.save(show)
            .let { ShowResponse.from(show) }
    }

    @Transactional
    fun updateShowImage(showId: Long, request: UpdateShowImageRequest): ShowResponse? {
        val show = showRepository.findByIdOrNull(showId) ?: throw RuntimeException()
        show.updateImageUrl(request.imageUrl)
        return ShowResponse.from(show)
    }

    fun deleteShowById(showId: Long) {
        val show = showRepository.findByIdOrNull(showId) ?: throw RuntimeException()
        showRepository.delete(show)
    }

    fun getAllShows(page: Int, size: Int): Page<ShowResponse> {
        val queryFactory = JPAQueryFactory(entityManager)
        val show = QShow.show
        val pageable = PageRequest.of(page, size)

        val query = queryFactory
            .selectFrom(show)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())

        val results = query.fetch()
        val total = query.fetchCount()
        val shows = results.map { ShowResponse.from(it) }

        return PageImpl(shows,pageable,total)
    }

    fun searchShowsByName(title: String, page: Int, size: Int): Page<ShowResponse> {
        val queryFactory = JPAQueryFactory(entityManager)
        val show = QShow.show
        val pageable = PageRequest.of(page,size)

        val predicate :BooleanExpression = if(title.isNotEmpty()){
            show.title.containsIgnoreCase(title)
        }else{
            Expressions.asBoolean(true).isTrue
        }

        val query = queryFactory
            .selectFrom(show)
            .where(predicate)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())

        val results = query.fetch()
        val shows = results.map{ShowResponse.from(it)}

        val total = queryFactory.selectFrom(show).where(predicate).fetchCount()

        return PageImpl(shows,pageable,total)
    }
}