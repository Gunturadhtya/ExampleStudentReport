package com.example.studentreport.entity

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "Report_Log")
data class ReportLog(

    @Id
    val id: UUID,

    @Column(name = "admin_id")
    val adminId: UUID,

    @Column(name = "report_id")
    val reportId: UUID,

    @Enumerated(EnumType.STRING)
    @Column(name = "old_status")
    val oldStatus: ReportStatus,

    @Enumerated(EnumType.STRING)
    @Column(name = "new_status")
    val newStatus: ReportStatus,

    @Column(name = "created_at")
    val createdAt: Instant,

    var notes: String?
)