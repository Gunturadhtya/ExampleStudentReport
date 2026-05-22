package com.example.studentreport.entity

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "Report_Image")
data class ReportImage(

    @Id
    val id: UUID,

    @Column(name = "report_id")
    val reportId: UUID,

    @Column(name = "image_url")
    val imageUrl: String,

    @Column(name = "uploaded_at")
    val uploadedAt: Instant
)