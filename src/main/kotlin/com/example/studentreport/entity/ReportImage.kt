package com.example.studentreport.entity

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "report_images")
data class ReportImage(
    @Id @GeneratedValue val id: UUID? = null,
    @Column(name = "report_id", insertable = false, updatable = false) val reportId: UUID,
    @Column(name = "image_url") val imageUrl: String,
    @Column(name = "uploaded_at") val uploadedAt: Instant,

    @ManyToOne
    @JoinColumn(name = "report_id")
    var report: Report? = null
)