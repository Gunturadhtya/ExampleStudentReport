package com.example.studentreport.seeder

import com.example.studentreport.entity.Report
import com.example.studentreport.entity.ReportStatus
import com.example.studentreport.repository.CategoryRepository
import com.example.studentreport.repository.ReportRepository
import com.example.studentreport.repository.RoomRepository
import com.example.studentreport.repository.UserRepository
import com.example.studentreport.repository.UserStatsRepository
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.random.Random

@Component
class ReportSeeder(
    private val reportRepository: ReportRepository,
    private val userRepository: UserRepository,
    private val categoryRepository: CategoryRepository,
    private val roomRepository: RoomRepository,
    private val userStatsRepository: UserStatsRepository
) {
    fun seed() {
        if (reportRepository.count() > 0) return

        val user = userRepository.findByEmail("student@mhs.ulm.ac.id") ?: return
        val categories = categoryRepository.findAll()
        val rooms = roomRepository.findAll()

        if (categories.isEmpty() || rooms.isEmpty()) return

        val userStats = userStatsRepository.findAll().firstOrNull { it.userId == user.id } ?: return
        val statuses = ReportStatus.entries.toTypedArray()
        val now = Instant.now()
        val reportsToSave = mutableListOf<Report>()

        for (i in 1..25) {
            val status = statuses[Random.nextInt(statuses.size)]
            val category = categories[Random.nextInt(categories.size)]
            val room = rooms[Random.nextInt(rooms.size)]
            val pastDate = now.minus(Random.nextLong(1, 30), ChronoUnit.DAYS)

            val report = Report(
                reporterId = user.id!!,
                categoryId = category.id!!,
                roomId = room.id!!,
                title = "Kendala ${category.name} #$i",
                description = "Terdapat masalah pada fasilitas di ${room.name}. Mohon segera ditindaklanjuti oleh teknisi terkait.",
                status = status,
                createdAt = pastDate,
                updatedAt = pastDate,
                user = user,
                category = category,
                room = room
            )
            reportsToSave.add(report)

            userStats.reportCount++
            when (status) {
                ReportStatus.PENDING -> userStats.pendingReport++
                ReportStatus.COMPLETED -> userStats.completedReport++
                ReportStatus.REJECTED -> userStats.rejectedReport++
                ReportStatus.IN_PROGRESS -> {}
            }
        }

        reportRepository.saveAll(reportsToSave)
        userStatsRepository.save(userStats)
    }
}