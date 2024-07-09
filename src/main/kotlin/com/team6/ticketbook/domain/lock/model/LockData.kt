package com.team6.ticketbook.domain.lock.model

import jakarta.persistence.*

@Entity
@Table(name = "lock_key")
class LockData(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "code")
    var code: String,

    @Column(name = "lock_num")
    var lockNum: Long = 0
) {
}