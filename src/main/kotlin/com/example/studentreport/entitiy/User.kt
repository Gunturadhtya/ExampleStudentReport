package com.example.studentreport.entity

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "Users")
data class User(

    @Id
    val id: UUID,

    val email: String,

    @Column(name = "password_hash")
    val passwordHash: String,

    @Column(name = "created_at")
    val createdAt: Instant,

    var name: String,

    @Enumerated(EnumType.STRING)
    var role: UserRole,

    @Column(name = "updated_at")
    var updatedAt: Instant
)