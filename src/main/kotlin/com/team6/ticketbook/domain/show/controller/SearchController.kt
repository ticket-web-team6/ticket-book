package com.team6.ticketbook.domain.show.controller

import com.team6.ticketbook.domain.show.dto.ShowResponse
import com.team6.ticketbook.domain.show.dto.TopSearchKeyword
import com.team6.ticketbook.domain.show.service.SearchService
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("shows/search")
class SearchController(private val searchService: SearchService) {

    @GetMapping("/top10")
    fun topSearch10(
        @RequestParam (defaultValue = "10") limit:Long
    ): ResponseEntity<Set<TopSearchKeyword>> = ResponseEntity
        .status(HttpStatus.OK)
        .body(searchService.topSearch10(limit))

    @GetMapping("/v1")
    fun searchByDB(
        @RequestParam term:String
    ): ResponseEntity<Page<ShowResponse>> = ResponseEntity
        .status(HttpStatus.OK)
        .body(searchService.searchByDB(term))

    @GetMapping("/v2")
    fun searchByRedis(
        @RequestParam term:String
    ): ResponseEntity<Page<ShowResponse>> = ResponseEntity
        .status(HttpStatus.OK)
        .body(searchService.searchByRedis(term))
}