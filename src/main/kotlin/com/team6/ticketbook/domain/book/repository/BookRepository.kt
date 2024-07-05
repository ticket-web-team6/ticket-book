package com.team6.ticketbook.domain.book.repository

import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.CaseBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import com.team6.ticketbook.domain.book.model.Book
import com.team6.ticketbook.domain.book.model.QBook
import com.team6.ticketbook.domain.seat.dto.SeatResponse
import com.team6.ticketbook.domain.seat.model.QSeat
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface BookRepository : JpaRepository<Book, Long>, BookQueryDslRepository {
    fun existsByShowIdAndDateAndSeatId(showId: Long, date: LocalDate, seatId: Long): Boolean
}

interface BookQueryDslRepository {
    fun findAllSeatsWithAvailability(
        date: LocalDate,
        placeId: Long,
    ): List<SeatResponse>
}

class BookQueryDslRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : BookQueryDslRepository {

    private val seat = QSeat.seat
    private val book = QBook.book

    override fun findAllSeatsWithAvailability(
        date: LocalDate,
        placeId: Long,
    ): List<SeatResponse> {
        val seatList = jpaQueryFactory
            .select(
                Projections.constructor(
                    SeatResponse::class.java,
                    seat.id,
                    seat.seatCode,
                    seat.grade,
                    CaseBuilder()
                        .`when`(book.id.isNull)
                        .then(false)
                        .otherwise(true)
                        .`as`("isTaken")
                )
            )
            .from(seat)
            .leftJoin(book)
            .on(seat.id.eq(book.seat().id).and(book.date.eq(date)))
            .where(
                placeEq(placeId)
            )
            .orderBy(seat.seatCode.asc())
            .fetch()

        return seatList
    }


    private fun placeEq(placeId: Long): BooleanExpression? {
        return seat.placeId.eq(placeId)
    }


}