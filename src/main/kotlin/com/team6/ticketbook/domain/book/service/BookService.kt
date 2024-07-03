package com.team6.ticketbook.domain.book.service

import com.team6.ticketbook.domain.book.dto.BookResponse
import com.team6.ticketbook.domain.book.dto.CreateBookRequest
import com.team6.ticketbook.domain.book.model.Book
import com.team6.ticketbook.domain.book.repository.BookRepository
import com.team6.ticketbook.domain.show.repository.ShowRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class BookService(
    private val bookRepository: BookRepository,
    private val showRepository: ShowRepository
) {
    fun getBookById(bookId: Long): BookResponse {
        val book = bookRepository.findByIdOrNull(bookId) ?: throw RuntimeException()
        return BookResponse.from(book)
    }

    @Transactional
    fun createBook(request: CreateBookRequest): BookResponse {
        val show = showRepository.findByIdOrNull(request.showId) ?: throw RuntimeException()
        return Book(
            show = show,
            memberId = request.memberId,
            seatCode = request.seatCode,
            date = request.date,
            price = request.price,
        ).let { bookRepository.save(it) }
            .let { BookResponse.from(it) }
    }

    @Transactional
    fun deleteBookById(bookId: Long) {
        val book = bookRepository.findByIdOrNull(bookId) ?: throw RuntimeException()
        bookRepository.delete(book)
    }
}