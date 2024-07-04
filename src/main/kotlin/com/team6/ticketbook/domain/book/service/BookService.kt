package com.team6.ticketbook.domain.book.service

import com.team6.ticketbook.domain.book.dto.BookResponse
import com.team6.ticketbook.domain.book.dto.CreateBookRequest
import com.team6.ticketbook.domain.book.exception.InvalidDateException
import com.team6.ticketbook.domain.book.exception.SeatAlreadyTakenException
import com.team6.ticketbook.domain.book.model.Book
import com.team6.ticketbook.domain.book.repository.BookRepository
import com.team6.ticketbook.domain.exception.InvalidCredentialException
import com.team6.ticketbook.domain.exception.ModelNotFoundException
import com.team6.ticketbook.domain.seat.repository.SeatRepository
import com.team6.ticketbook.domain.show.model.Show
import com.team6.ticketbook.domain.show.repository.ShowRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class BookService(
    private val bookRepository: BookRepository,
    private val showRepository: ShowRepository,
    private val seatRepository: SeatRepository
) {
    fun getBookById(memberId: Long, bookId: Long): BookResponse {
        val book = bookRepository.findByIdOrNull(bookId) ?: throw ModelNotFoundException("book", bookId)
        if (book.memberId != memberId) throw InvalidCredentialException()
        return BookResponse.from(book)
    }

    @Transactional
    fun createBook(memberId: Long, request: CreateBookRequest): BookResponse {
        val show = showRepository.findByIdOrNull(request.showId) ?: throw ModelNotFoundException("show", request.showId)
        if (show.startDate > request.date || show.endDate < request.date) throw InvalidDateException()
        val seat = seatRepository.findByIdOrNull(request.seatId) ?: throw ModelNotFoundException("seat", request.seatId)
        val price = calculatePrice(seat.grade, show)
        if (bookRepository.existsByShowIdAndDateAndSeatId(
                showId = request.showId,
                date = request.date,
                seatId = request.seatId,
            )
        ) throw SeatAlreadyTakenException()
        return Book(
            show = show,
            memberId = memberId,
            seat = seat,
            date = request.date,
            price = price,
        ).let { bookRepository.save(it) }
            .let { BookResponse.from(it) }
    }

    @Transactional
    fun deleteBookById(memberId: Long, bookId: Long) {
        val book = bookRepository.findByIdOrNull(bookId) ?: throw ModelNotFoundException("book", bookId)
        if (book.memberId != memberId) throw InvalidCredentialException()
        bookRepository.delete(book)
    }

    private fun calculatePrice(grade: String, show: Show): Int {
        return when (grade) {
            "VIP" -> show.vipPrice
            "R" -> show.rPrice
            "S" -> show.sPrice
            "A" -> show.aPrice
            else -> throw RuntimeException()
        }
    }

}