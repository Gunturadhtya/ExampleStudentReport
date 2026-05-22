package com.example.studentreport.entity

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "Rooms")
data class Room(

    @Id
    val id: UUID,

    @Column(name = "building_id")
    val buildingId: UUID,

    @Column(name = "created_at")
    val createdAt: Instant,

    var name: String,

    var floor: Int,

    var code: String,

    @Column(name = "updated_at")
    var updatedAt: Instant
)