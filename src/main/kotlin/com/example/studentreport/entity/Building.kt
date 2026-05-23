package com.example.studentreport.entity

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "buildings")
data class Building(
    @Id @GeneratedValue val id: UUID? = null,
    @Column(name = "created_at") val createdAt: Instant,
    var name: String,
    var code: String,
    @Column(name = "updated_at") var updatedAt: Instant,

    @OneToMany(mappedBy = "building", cascade = [CascadeType.ALL])
    var rooms: MutableList<Room> = mutableListOf()
) {
    fun addRoom(room: Room) {
        rooms.add(room)
        room.building = this
    }
}