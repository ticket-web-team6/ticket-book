package com.team6.ticketbook.domain.show.controller

import com.team6.ticketbook.domain.show.dto.CreateShowRequest
import com.team6.ticketbook.domain.show.dto.ShowResponse
import com.team6.ticketbook.domain.show.dto.UpdateShowImageRequest
import com.team6.ticketbook.domain.show.service.ShowService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/shows")
class ShowController(
    private val showService: ShowService
) {

    @GetMapping("/{showId}")
    fun getShowById(@PathVariable("showId") showId: Long): ResponseEntity<ShowResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(showService.getShowById(showId))
    }

    @PostMapping
    fun createShow(@RequestBody request: CreateShowRequest): ResponseEntity<ShowResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(showService.createShow(request))
    }

    @PatchMapping("/{showId}")
    fun updateShowImage(
        @PathVariable("showId") showId: Long,
        @RequestBody request: UpdateShowImageRequest
    ): ResponseEntity<ShowResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(showService.updateShowImage(showId, request))
    }

    @DeleteMapping("/{showId}")
    fun deleteShowById(@PathVariable("showId") showId: Long): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(showService.deleteShowById(showId))
    }
}