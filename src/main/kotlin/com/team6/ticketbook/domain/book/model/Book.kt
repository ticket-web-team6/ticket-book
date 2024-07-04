package com.team6.ticketbook.domain.book.model

import com.team6.ticketbook.domain.show.model.Show
import jakarta.persistence.*
import java.time.LocalDate


@Entity
@Table(name = "book")
class Book(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "show_id")
    val show: Show,

    @Column(name = "member_id")
    val memberId: Long,

    @Column(name = "seat_id")
    val seatId: Long,

    @Column(name = "date")
    val date: LocalDate,

    @Column(name = "price")
    val price: Int,
) {
}