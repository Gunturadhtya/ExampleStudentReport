package com.example.studentreport.entity

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "student_data")
data class StudentData(
    @Id @GeneratedValue val id: UUID? = null,
    @Column(name = "user_id") val userId: UUID,
    var nim: String,
    var faculty: String,
    var major: String,
    var year: Int,

    @OneToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    var user: User? = null
)