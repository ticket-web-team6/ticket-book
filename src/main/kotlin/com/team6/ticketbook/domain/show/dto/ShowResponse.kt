package com.team6.ticketbook.domain.show.dto

import com.team6.ticketbook.domain.place.dto.PlaceResponse
import com.team6.ticketbook.domain.show.model.Show
import java.time.LocalDate

data class ShowResponse(
    val id: Long,
    val title: String,
    val startTime: String,
    val vipPrice: Int,
    val rPrice: Int,
    val sPrice: Int,
    val aPrice: Int,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val mainImageUrl: String,
    val info: String,
    val category: String,
    val place: PlaceResponse
) {
    companion object {
        fun from(show: Show): ShowResponse {
            return ShowResponse(
                id = show.id!!,
                title = show.title,
                startTime = show.startTime.name,
                vipPrice = show.vipPrice,
                rPrice = show.rPrice,
                sPrice = show.sPrice,
                aPrice = show.aPrice,
                startDate = show.startDate,
                endDate = show.endDate,
                mainImageUrl = show.mainImageUrl,
                info = show.info,
                category = show.category,
                place = PlaceResponse.from(show.place)
            )
        }
    }
}

data class ShowResponseWithBook(
    val title: String,
    val startTime: String,
    val mainImageUrl: String,
    val category: String,
    val place: PlaceResponse
) {
    companion object {
        fun from(show: Show): ShowResponseWithBook {
            return ShowResponseWithBook(
                title = show.title,
                startTime = show.startTime.name,
                mainImageUrl = show.mainImageUrl,
                category = show.category,
                place = PlaceResponse.from(show.place)

            )
        }
    }
}