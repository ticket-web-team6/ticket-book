package com.team6.ticketbook.domain.book.repository

import com.team6.ticketbook.domain.book.model.Book
import org.springframework.data.jpa.repository.JpaRepository

interface BookRepository : JpaRepository<Book, Long> {
}