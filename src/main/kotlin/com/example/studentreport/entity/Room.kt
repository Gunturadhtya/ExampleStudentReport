package com.example.studentreport.entity

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "rooms")
data class Room(
    @Id @GeneratedValue val id: UUID? = null,
    @Column(name = "building_id", insertable = false, updatable = false) val buildingId: UUID,
    val createdAt: Instant,
    var name: String,
    var floor: Int,
    var code: String,
    @Column(name = "updated_at") var updatedAt: Instant,

    @ManyToOne
    @JoinColumn(name = "building_id")
    var building: Building? = null,

    @OneToMany(mappedBy = "room", cascade = [CascadeType.ALL])
    var reports: MutableList<Report> = mutableListOf()
)