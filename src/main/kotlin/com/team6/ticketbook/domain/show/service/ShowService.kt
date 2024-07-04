package com.team6.ticketbook.domain.show.service

import com.team6.ticketbook.domain.book.exception.InvalidDateException
import com.team6.ticketbook.domain.book.repository.BookRepository
import com.team6.ticketbook.domain.exception.ModelNotFoundException
import com.team6.ticketbook.domain.place.model.Place
import com.team6.ticketbook.domain.place.repository.PlaceRepository
import com.team6.ticketbook.domain.seat.dto.SeatResponse
import com.team6.ticketbook.domain.show.dto.CreateShowRequest
import com.team6.ticketbook.domain.show.dto.ShowResponse
import com.team6.ticketbook.domain.show.dto.ShowSearchFilter
import com.team6.ticketbook.domain.show.dto.UpdateShowImageRequest
import com.team6.ticketbook.domain.show.model.Show
import com.team6.ticketbook.domain.show.repository.ShowRepository
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ShowService(
    private val showRepository: ShowRepository,
    private val bookRepository: BookRepository,
    private val placeRepository: PlaceRepository
) {
    fun getShowById(showId: Long): ShowResponse {
        val show = showRepository.findByIdOrNull(showId) ?: throw ModelNotFoundException("show", showId)
        return ShowResponse.from(show)
    }

    @Transactional
    fun createShow(request: CreateShowRequest): ShowResponse? {
        val place = placeRepository.findByIdOrNull(request.placeId)
            ?: throw ModelNotFoundException("Place", request.placeId)
        val show = createShowFromRequestAndPlace(request, place)
        return showRepository.save(show)
            .let { ShowResponse.from(show) }
    }

    @Transactional
    fun updateShowImage(showId: Long, request: UpdateShowImageRequest): ShowResponse? {
        val show = showRepository.findByIdOrNull(showId) ?: throw ModelNotFoundException("show", showId)
        show.updateImageUrl(request.imageUrl)
        return ShowResponse.from(show)
    }

    fun deleteShowById(showId: Long) {
        val show = showRepository.findByIdOrNull(showId) ?: throw ModelNotFoundException("show", showId)
        showRepository.delete(show)
    }

    fun getAvailableSeats(showId: Long, date: LocalDate): List<SeatResponse>? {
        val show = showRepository.findByIdOrNull(showId) ?: throw ModelNotFoundException("show", showId)
        if (show.startDate > date || show.endDate < date) throw InvalidDateException()
        return bookRepository.findAllSeatsWithAvailability(date, show.place.id!!)
    }

    fun getAllShows(pageable: Pageable): Page<ShowResponse> {
        return showRepository.findAllPaginated(pageable)
            .map { ShowResponse.from(it) }
    }

    fun searchShowsByFilter(filter: ShowSearchFilter, pageable: Pageable): Page<ShowResponse> {
        return showRepository.findByFilterPaginated(filter, pageable)
            .map { ShowResponse.from(it) }
    }

    private fun createShowFromRequestAndPlace(request: CreateShowRequest, place: Place): Show {
        return Show(
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
            place = place
        )
    }
}