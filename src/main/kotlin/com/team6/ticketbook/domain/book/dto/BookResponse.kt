package com.team6.ticketbook.domain.book.dto

import com.team6.ticketbook.domain.book.model.Book
import com.team6.ticketbook.domain.show.dto.ShowResponse
import java.time.LocalDate

data class BookResponse(
    val id: Long,
    val show: ShowResponse,
    val memberId: Long,
    val seatCode: String,
    val date: LocalDate,
    val price: Int
) {
    companion object {
        fun from(book: Book): BookResponse {
            return BookResponse(
                id = book.id!!,
                show = ShowResponse.from(book.show),
                memberId = book.memberId,
                seatCode = book.seat.seatCode,
                date = book.date,
                price = book.price
            )
        }
    }
}
