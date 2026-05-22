package com.example.studentreport.entity

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "Upvotes")
data class Upvote(

    @Id
    val id: UUID,

    @Column(name = "user_id")
    val userId: UUID,

    @Column(name = "report_id")
    val reportId: UUID,

    @Column(name = "created_at")
    val createdAt: Instant
)