package com.team6.ticketbook.domain.member.model

import jakarta.persistence.*

@Entity
@Table(name = "member")
class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "email")
    val email: String,

    @Column(name = "password")
    var password: String,

    @Column(name = "name")
    val name: String,

    @Column(name = "address")
    var address: String,

    @Column(name = "phone_number")
    val phoneNumber: String
) {
    fun updateAddress(address: String) {
        this.address = address
    }
}