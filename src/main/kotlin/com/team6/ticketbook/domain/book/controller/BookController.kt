package com.team6.ticketbook.domain.book.controller

import com.team6.ticketbook.domain.book.dto.BookResponse
import com.team6.ticketbook.domain.book.dto.CreateBookRequest
import com.team6.ticketbook.domain.book.service.BookService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/books")
class BookController(
    private val bookService: BookService
) {

    @GetMapping("/{bookId}")
    fun getBookById(@PathVariable bookId: Long): ResponseEntity<BookResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(bookService.getBookById(bookId))
    }

    @PostMapping
    fun createBook(@RequestBody request: CreateBookRequest): ResponseEntity<BookResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(bookService.createBook(request))
    }

    @DeleteMapping("/{bookId}")
    fun deleteBookById(@PathVariable bookId: Long): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(bookService.deleteBookById(bookId))
    }
}