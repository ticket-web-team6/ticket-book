package com.team6.ticketbook.domain.show.dto

import java.time.LocalDate

data class ShowSearchFilter(
    val title: String? = null,
    val category: String? = null,
    val date: LocalDate? = null,
)
