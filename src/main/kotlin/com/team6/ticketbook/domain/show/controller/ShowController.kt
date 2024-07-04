package com.team6.ticketbook.domain.show.controller

import com.team6.ticketbook.domain.show.dto.CreateShowRequest
import com.team6.ticketbook.domain.show.dto.ShowResponse
import com.team6.ticketbook.domain.show.dto.UpdateShowImageRequest
import com.team6.ticketbook.domain.show.service.ShowService
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/shows")
class ShowController(
    private val showService: ShowService
) {

    @GetMapping("/{showId}")
    fun getShowById(
        @PathVariable("showId") showId: Long
    ): ResponseEntity<ShowResponse> = ResponseEntity
        .status(HttpStatus.OK)
        .body(showService.getShowById(showId))

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    fun createShow(
        @RequestBody request: CreateShowRequest
    ): ResponseEntity<ShowResponse> = ResponseEntity
        .status(HttpStatus.CREATED)
        .body(showService.createShow(request))

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PatchMapping("/{showId}")
    fun updateShowImage(
        @PathVariable("showId") showId: Long,
        @RequestBody request: UpdateShowImageRequest
    ): ResponseEntity<ShowResponse> = ResponseEntity
        .status(HttpStatus.OK)
        .body(showService.updateShowImage(showId, request))

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping("/{showId}")
    fun deleteShowById(
        @PathVariable("showId") showId: Long
    ): ResponseEntity<Unit> = ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .body(showService.deleteShowById(showId))

    @GetMapping("/all")
    fun getAllShows(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "5") size: Int,
    ): ResponseEntity<Page<ShowResponse>> = ResponseEntity
        .status(HttpStatus.OK)
        .body(showService.getAllShows(page, size))

    @GetMapping("/search")
    fun searchShowsByName(
        @RequestParam(defaultValue = "") title: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "5") size: Int,
    ): ResponseEntity<Page<ShowResponse>> = ResponseEntity
        .status(HttpStatus.OK)
        .body(showService.searchShowsByName(title, page, size))
}