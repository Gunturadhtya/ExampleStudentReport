package com.example.studentreport.entity

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "User_Stats")
data class UserStats(

    @Id
    val id: UUID,

    @Column(name = "user_id")
    val userId: UUID,

    @Column(name = "report_count")
    var reportCount: Int,

    @Column(name = "pending_report")
    var pendingReport: Int,

    @Column(name = "completed_report")
    var completedReport: Int,

    @Column(name = "rejected_report")
    var rejectedReport: Int,

    @Column(name = "updated_at")
    var updatedAt: Instant
)