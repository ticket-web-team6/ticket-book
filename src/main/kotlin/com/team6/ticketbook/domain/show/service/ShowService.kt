package com.team6.ticketbook.domain.show.service

import com.team6.ticketbook.domain.show.dto.CreateShowRequest
import com.team6.ticketbook.domain.show.dto.ShowResponse
import com.team6.ticketbook.domain.show.dto.UpdateShowImageRequest
import com.team6.ticketbook.domain.show.model.Show
import com.team6.ticketbook.domain.show.repository.ShowRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ShowService(
    private val showRepository: ShowRepository,
) {
    fun getShowById(showId: Long): ShowResponse {
        val show = showRepository.findByIdOrNull(showId) ?: throw RuntimeException()
        return ShowResponse.from(show)
    }

    @Transactional
    fun createShow(request: CreateShowRequest): ShowResponse? {
        val show = Show(
            title = request.title,
            vipPrice = request.vipPrice,
            rPrice = request.rPrice,
            sPrice = request.sPrice,
            aPrice = request.aPrice,
            startDate = request.startDate,
            endDate = request.endDate,
            startTime = request.startTime,
            mainImageUrl = request.mainImageUrl,
            info = request.info,
            category = request.category,
        )
        // Todo : Seat 생성 로직 구현 필요 
        return showRepository.save(show)
            .let { ShowResponse.from(show) }
    }

    @Transactional
    fun updateShowImage(showId: Long, request: UpdateShowImageRequest): ShowResponse? {
        val show = showRepository.findByIdOrNull(showId) ?: throw RuntimeException()
        show.updateImageUrl(request.imageUrl)
        return ShowResponse.from(show)
    }

    fun deleteShowById(showId: Long) {
        val show = showRepository.findByIdOrNull(showId) ?: throw RuntimeException()
        showRepository.delete(show)
    }


}