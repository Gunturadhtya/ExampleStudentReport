package com.example.studentreport.entity

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "reports")
data class Report(
    @Id @GeneratedValue val id: UUID? = null,
    @Column(name = "reporter_id", insertable = false, updatable = false) val reporterId: UUID,
    @Column(name = "category_id", insertable = false, updatable = false) val categoryId: UUID,
    @Column(name = "created_at") val createdAt: Instant,
    @Version var version: Long = 0L,
    @Column(name = "room_id", insertable = false, updatable = false) var roomId: UUID,
    var title: String,
    var description: String,
    @Enumerated(EnumType.STRING) var status: ReportStatus,
    @Column(name = "updated_at") var updatedAt: Instant,
    @Column(name = "deleted_at") var deletedAt: Instant? = null,

    @ManyToOne
    @JoinColumn(name = "reporter_id")
    var user: User? = null,

    @ManyToOne
    @JoinColumn(name = "room_id")
    var room: Room? = null,

    @ManyToOne
    @JoinColumn(name = "category_id")
    var category: Category? = null,

    @OneToMany(mappedBy = "report", cascade = [CascadeType.ALL])
    var images: MutableList<ReportImage> = mutableListOf(),

    @OneToMany(mappedBy = "report", cascade = [CascadeType.ALL])
    var upvotes: MutableList<Upvote> = mutableListOf(),

    @OneToMany(mappedBy = "report", cascade = [CascadeType.ALL])
    var logs: MutableList<ReportLog> = mutableListOf()
) {
    fun addImage(image: ReportImage) {
        images.add(image)
        image.report = this
    }

    fun addUpvote(upvote: Upvote) {
        upvotes.add(upvote)
        upvote.report = this
    }

    fun addLog(log: ReportLog) {
        logs.add(log)
        log.report = this
    }

    fun softDelete() {
        deletedAt = Instant.now()
    }

    fun isDeleted(): Boolean {
        return deletedAt != null
    }
}