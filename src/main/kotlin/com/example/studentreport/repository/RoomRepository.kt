package com.example.studentreport.repository

import com.example.studentreport.entity.Room
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface RoomRepository : JpaRepository<Room, UUID> {
}