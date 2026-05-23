package com.example.studentreport.entity

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "upvotes")
data class Upvote(
    @Id @GeneratedValue val id: UUID? = null,
    @Column(name = "user_id", insertable = false, updatable = false) val userId: UUID,
    @Column(name = "report_id", insertable = false, updatable = false) val reportId: UUID,
    @Column(name = "created_at") val createdAt: Instant,

    @ManyToOne
    @JoinColumn(name = "report_id")
    var report: Report? = null,

    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: User? = null
)