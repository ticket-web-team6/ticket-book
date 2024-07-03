package com.team6.ticketbook.domain.show.model

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "concert")
class Show(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "title")
    val title: String,

    @Column(name = "vip_price")
    val vipPrice: Int,

    @Column(name = "r_price")
    val rPrice: Int,

    @Column(name = "s_price")
    val sPrice: Int,

    @Column(name = "a_price")
    val aPrice: Int,

    @Column(name = "start_date")
    val startDate: LocalDate,

    @Column(name = "end_date")
    val endDate: LocalDate,

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "start_time")
    val startTime: ShowStartTime,

    @Column(name = "main_image_url")
    var mainImageUrl: String,

    @Column(name = "info")
    val info: String,

    @Column(name = "category")
    val category: String,

    ) {

    fun updateImageUrl(imageUrl: String) {
        this.mainImageUrl = imageUrl
    }
}