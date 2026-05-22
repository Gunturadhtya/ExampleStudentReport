package com.example.studentreport.entity

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "Reports")
data class Report(

    @Id
    val id: UUID,

    @Column(name = "reporter_id")
    val reporterId: UUID,

    @Column(name = "category_id")
    val categoryId: UUID,

    @Column(name = "created_at")
    val createdAt: Instant,

    var version: Long,

    @Column(name = "room_id")
    var roomId: UUID,

    var title: String,

    var description: String,

    @Enumerated(EnumType.STRING)
    var status: ReportStatus,

    @Column(name = "updated_at")
    var updatedAt: Instant,

    @Column(name = "deleted_at")
    var deletedAt: Instant?,

    @OneToMany
    @JoinColumn(name = "report_id")
    var images: MutableList<ReportImage> = mutableListOf(),

    @OneToMany
    @JoinColumn(name = "report_id")
    var upvotes: MutableList<Upvote> = mutableListOf(),

    @OneToMany
    @JoinColumn(name = "report_id")
    var logs: MutableList<ReportLog> = mutableListOf()
) {

    fun addImage(image: ReportImage) {
        images.add(image)
    }

    fun addUpvote(upvote: Upvote) {
        upvotes.add(upvote)
    }

    fun addLog(log: ReportLog) {
        logs.add(log)
    }

    fun softDelete() {
        deletedAt = Instant.now()
    }

    fun isDeleted(): Boolean {
        return deletedAt != null
    }
}