package com.example.studentreport.entity

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "categories")
data class Category(
    @Id @GeneratedValue val id: UUID? = null,
    @Column(name = "created_at") val createdAt: Instant,
    var name: String,
    var description: String,
    @Column(name = "updated_at") var updatedAt: Instant,

    @OneToMany(mappedBy = "category", cascade = [CascadeType.ALL])
    var reports: MutableList<Report> = mutableListOf()
)