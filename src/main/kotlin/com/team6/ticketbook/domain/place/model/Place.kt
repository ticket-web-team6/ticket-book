package com.team6.ticketbook.domain.place.model

import jakarta.persistence.*

@Entity
@Table(name = "place")
class Place(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "name")
    val name: String,

    @Column(name = "address")
    val address: String,
) {
}