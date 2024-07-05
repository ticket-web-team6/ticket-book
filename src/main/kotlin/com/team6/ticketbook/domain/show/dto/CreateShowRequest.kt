package com.team6.ticketbook.domain.show.dto

import com.team6.ticketbook.domain.show.model.ShowStartTime
import java.time.LocalDate

data class CreateShowRequest(
    val title: String,
    val startTime: ShowStartTime,
    val vipPrice: Int,
    val rPrice: Int,
    val sPrice: Int,
    val aPrice: Int,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val mainImageUrl: String,
    val info: String,
    val category: String,
    val placeId: Long,
)