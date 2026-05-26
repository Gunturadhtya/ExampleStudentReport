package com.example.studentreport.seeder

import com.example.studentreport.entity.Room
import com.example.studentreport.repository.BuildingRepository
import com.example.studentreport.repository.RoomRepository
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class RoomSeeder(
    private val roomRepository: RoomRepository,
    private val buildingRepository: BuildingRepository
) {
    fun seed() {
        if (roomRepository.count() > 0) return

        val building = buildingRepository.findAll().firstOrNull() ?: return
        val now = Instant.now()

        val rooms = listOf(
            Room(buildingId = building.id!!, name = "Ruang A-14", floor = 1, code = "FT-A14", createdAt = now, updatedAt = now, building = building),
            Room(buildingId = building.id!!, name = "Ruang A-15", floor = 1, code = "FT-A15", createdAt = now, updatedAt = now, building = building),
            Room(buildingId = building.id!!, name = "Lab Komputer 1", floor = 2, code = "FT-LAB1", createdAt = now, updatedAt = now, building = building)
        )
        roomRepository.saveAll(rooms)
    }
}