package com.example.studentreport.entity

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "sessions")
data class Session(
    @Id @GeneratedValue val id: UUID? = null,
    @Column(name = "user_id", insertable = false, updatable = false) val userId: UUID,
    @Column(name = "token", nullable = false) val token: String,
    @Column(name = "expires_at", nullable = false) var expiresAt: Instant,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User? = null
)
{
    @Transient
    val createdAt: Instant = Instant.now()

    fun isExpired(): Boolean {
        return Instant.now().isAfter(expiresAt)
    }

    fun invalidate() {
        expiresAt = Instant.now()
    }
}