package com.example.studentreport.repository

import com.example.studentreport.entity.Session
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface SessionRepository : JpaRepository<Session, UUID> {
    fun findByToken(token: String): Session?
}