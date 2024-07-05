package com.team6.ticketbook.domain.place.dto

import com.team6.ticketbook.domain.place.model.Place

data class PlaceResponse(
    val id: Long,
    val name: String,
    val address: String,
) {
    companion object {
        fun from(place: Place): PlaceResponse {
            return PlaceResponse(
                id = place.id!!,
                name = place.name,
                address = place.address,
            )
        }
    }
}