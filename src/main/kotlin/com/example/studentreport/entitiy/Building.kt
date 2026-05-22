package com.example.studentreport.entity

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "Buildings")
data class Building(

    @Id
    val id: UUID,

    @Column(name = "created_at")
    val createdAt: Instant,

    var name: String,

    var code: String,

    @Column(name = "updated_at")
    var updatedAt: Instant,

    @OneToMany
    @JoinColumn(name = "building_id")
    var rooms: MutableList<Room> = mutableListOf()
) {

    fun addRoom(room: Room) {
        rooms.add(room)
    }
}