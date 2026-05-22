package com.example.studentreport.entity

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "Idempotency_Keys")
data class IdempotencyKey(

    @Id
    val id: Int,

    val key: String,

    @Column(name = "user_id")
    val userId: UUID,

    @Column(name = "request_path")
    val requestPath: String,

    @Column(name = "response_status")
    val responseStatus: Short,

    @Column(name = "response_body")
    val responseBody: String,

    @Column(name = "created_at")
    val createdAt: Instant,

    @Column(name = "expires_at")
    val expiresAt: Instant
) {

    fun isExpired(): Boolean {
        return expiresAt.isBefore(Instant.now())
    }
}