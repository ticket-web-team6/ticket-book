package com.team6.ticketbook.domain.place.repository

import com.team6.ticketbook.domain.place.model.Place
import org.springframework.data.jpa.repository.JpaRepository

interface PlaceRepository : JpaRepository<Place, Long> {

}