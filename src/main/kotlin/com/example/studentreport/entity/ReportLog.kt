package com.example.studentreport.entity

import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "report_logs")
data class ReportLog(
    @Id @GeneratedValue
    val id: UUID? = null,

    @Column(name = "admin_id", insertable = false, updatable = false)
    val adminId: UUID,

    @Column(name = "report_id", insertable = false, updatable = false)
    val reportId: UUID,

    @Enumerated(EnumType.STRING) @Column(name = "old_status") @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    val oldStatus: ReportStatus,

    @Enumerated(EnumType.STRING) @Column(name = "new_status") @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    val newStatus: ReportStatus,

    @Column(name = "created_at")
    val createdAt: Instant,

    var notes: String? = null,

    @ManyToOne
    @JoinColumn(name = "report_id")
    var report: Report? = null,

    @ManyToOne
    @JoinColumn(name = "admin_id")
    var admin: User? = null
)