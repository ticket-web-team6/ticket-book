package com.team6.ticketbook.domain.book.controller

import com.team6.ticketbook.domain.book.dto.BookResponse
import com.team6.ticketbook.domain.book.dto.CreateBookRequest
import com.team6.ticketbook.domain.book.service.BookService
import com.team6.ticketbook.infra.security.MemberPrincipal
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/books")
class BookController(
    private val bookService: BookService
) {

    @GetMapping("/{bookId}")
    fun getBookById(
        @PathVariable bookId: Long,
        @AuthenticationPrincipal member: MemberPrincipal
    ): ResponseEntity<BookResponse> = ResponseEntity
        .status(HttpStatus.OK)
        .body(bookService.getBookById(member.id, bookId))


    @PostMapping("/v1")
    fun createBook(
        @RequestBody request: CreateBookRequest,
        @AuthenticationPrincipal member: MemberPrincipal
    ): ResponseEntity<BookResponse> = ResponseEntity
        .status(HttpStatus.CREATED)
        .body(bookService.createBook(member.id, request))

    @PostMapping("/v2")
    fun createBookWithRedisLock(
        @RequestBody request: CreateBookRequest,
        @AuthenticationPrincipal member: MemberPrincipal
    ): ResponseEntity<BookResponse> = ResponseEntity
        .status(HttpStatus.CREATED)
        .body(bookService.createBookWithRedisLock(member.id, request))

    @PostMapping("/v3")
    fun createBookWithMySqlLock(
        @RequestBody request: CreateBookRequest,
        @AuthenticationPrincipal member: MemberPrincipal
    ): ResponseEntity<BookResponse> = ResponseEntity
        .status(HttpStatus.CREATED)
        .body(bookService.createBookWithJpaLock(member.id, request))

    @DeleteMapping("/{bookId}")
    fun deleteBookById(
        @PathVariable bookId: Long,
        @AuthenticationPrincipal member: MemberPrincipal
    ): ResponseEntity<Unit> = ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .body(bookService.deleteBookById(member.id, bookId))
}