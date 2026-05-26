package com.example.studentreport.seeder

import com.example.studentreport.entity.Building
import com.example.studentreport.repository.BuildingRepository
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class BuildingSeeder(
    private val buildingRepository: BuildingRepository
) {
    fun seed() {
        if (buildingRepository.count() > 0) return

        val now = Instant.now()
        val building = Building(
            name = "Gedung Fakultas Teknik",
            code = "FT-ULM",
            createdAt = now,
            updatedAt = now
        )
        buildingRepository.save(building)
    }
}