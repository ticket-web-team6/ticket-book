package com.team6.ticketbook.domain.show.dto

import java.io.Serializable

data class TopSearchKeyword(
    val keyword: String?,
    val score:Double?
): Serializable
