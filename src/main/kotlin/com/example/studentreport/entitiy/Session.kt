package com.example.studentreport.entity

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "Sessions")
data class Session(

    @Id
    val id: UUID,

    @Column(name = "user_id")
    val userId: UUID,

    val token: String,

    @Column(name = "created_at")
    val createdAt: Instant,

    @Column(name = "expires_at")
    var expiresAt: Instant
)