package com.example.studentreport.entity

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "categories")
data class Category(
    @Id @GeneratedValue val id: UUID? = null,
    @Column(name = "created_at") val createdAt: Instant = Instant.now(),
    var name: String,
    var description: String,
    @Column(name = "updated_at") var updatedAt: Instant = Instant.now(),

    @OneToMany(mappedBy = "category", cascade = [CascadeType.ALL])
    var reports: MutableList<Report> = mutableListOf()
)