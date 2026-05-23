package com.example.studentreport.entity

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "sessions")
data class Session(
    @Id @GeneratedValue val id: UUID? = null,
    @Column(name = "user_id", insertable = false, updatable = false) val userId: UUID,
    val token: String,
    @Column(name = "created_at") val createdAt: Instant,
    var expiresAt: Instant,

    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: User? = null
) {
    fun isExpired(): Boolean {
        return Instant.now().isAfter(expiresAt)
    }

    fun invalidate() {
        expiresAt = Instant.now()
    }
}