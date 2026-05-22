package com.example.studentreport.entity

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "Student_Data")
data class StudentData(

    @Id
    val id: UUID,

    @Column(name = "user_id")
    val userId: UUID,

    var nim: String,

    var faculty: String,

    var major: String,

    var year: Int
)