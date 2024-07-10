package com.team6.ticketbook.domain.show.service

import com.team6.ticketbook.domain.show.dto.ShowResponse
import com.team6.ticketbook.domain.show.dto.TopSearchKeyword
import com.team6.ticketbook.domain.show.repository.ShowRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class SearchService(
    private val showRepository: ShowRepository
) {
    private val pageRequest = PageRequest.of(0, 10)

    fun topSearch10(limit: Long): Set<TopSearchKeyword> {
        return showRepository.getTopSearchList(limit)
    }

    fun searchByDB(term: String): Page<ShowResponse> {
        showRepository.saveTermTopSearch(term)
        return showRepository.findByTitlePaginated(term, pageRequest)
            .let { showInfos ->
                showInfos.map { ShowResponse.from(it) }
            }
    }

    fun searchByRedis(term: String): Page<ShowResponse> {
        showRepository.saveTermTopSearch(term)
        return showRepository.getSearchDataWithTerm(term) ?: run {
            showRepository.findByTitlePaginated(term, pageRequest)
                .map { ShowResponse.from(it) }
                .also { showRepository.saveSearchDataWithTerm(term, it) }
        }
    }

}