package com.team6.ticketbook.domain.show.repository

import com.team6.ticketbook.domain.show.model.Show
import org.springframework.data.jpa.repository.JpaRepository

interface ShowRepository : JpaRepository<Show, Long> {
}