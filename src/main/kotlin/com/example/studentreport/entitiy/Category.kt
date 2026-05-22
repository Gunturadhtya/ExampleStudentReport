package com.example.studentreport.entity

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "Categories")
data class Category(

    @Id
    val id: UUID,

    @Column(name = "created_at")
    val createdAt: Instant,

    var name: String,

    var description: String,

    @Column(name = "updated_at")
    var updatedAt: Instant
)