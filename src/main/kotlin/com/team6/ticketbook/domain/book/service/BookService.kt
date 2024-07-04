package com.team6.ticketbook.domain.book.service

import com.team6.ticketbook.domain.book.dto.BookResponse
import com.team6.ticketbook.domain.book.dto.CreateBookRequest
import com.team6.ticketbook.domain.book.exception.SeatAlreadyTakenException
import com.team6.ticketbook.domain.book.model.Book
import com.team6.ticketbook.domain.book.repository.BookRepository
import com.team6.ticketbook.domain.exception.InvalidCredentialException
import com.team6.ticketbook.domain.exception.ModelNotFoundException
import com.team6.ticketbook.domain.seat.repository.SeatRepository
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
        val seat = seatRepository.findByIdOrNull(request.seatId) ?: throw ModelNotFoundException("seat", request.seatId)
        if (bookRepository.existsByShowIdAndDateAndSeatId(
                showId = request.showId,
                date = request.date,
                seatId = request.seatId,
            )
        ) throw SeatAlreadyTakenException()
        return Book(
            show = show,
            memberId = request.memberId,
            seat = seat,
            date = request.date,
            price = request.price,
        ).let { bookRepository.save(it) }
            .let { BookResponse.from(it) }
    }

    @Transactional
    fun deleteBookById(memberId: Long, bookId: Long) {
        val book = bookRepository.findByIdOrNull(bookId) ?: throw ModelNotFoundException("book", bookId)
        if (book.memberId != memberId) throw InvalidCredentialException()
        bookRepository.delete(book)
    }

}