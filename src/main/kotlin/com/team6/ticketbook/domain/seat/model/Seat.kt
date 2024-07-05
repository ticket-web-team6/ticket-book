package com.team6.ticketbook.domain.seat.model

import jakarta.persistence.*

@Entity
@Table(name = "seat")
class Seat(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "place_id")
    var placeId: Long,

    @Column(name = "seat_code")
    var seatCode: String,

    @Column(name = "grade")
    var grade: String,
) {
}