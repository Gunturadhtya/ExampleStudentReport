package com.example.studentreport.web.service.impl

import com.example.studentreport.entity.Report
import com.example.studentreport.entity.ReportStatus
import com.example.studentreport.repository.ReportRepository
import com.example.studentreport.web.service.DashboardService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class DashboardServiceImpl(
    private val reportRepository: ReportRepository
) : DashboardService {

    override fun getUserDashboardStats(): Map<String, Int> {
        val allReports = reportRepository.findAll()

        val pendingCount = allReports.count {
            it.status == ReportStatus.PENDING || it.status == ReportStatus.IN_PROGRESS
        }

        val completedCount = allReports.count {
            it.status == ReportStatus.COMPLETED
        }

        return mapOf(
            "reportCount" to allReports.size,
            "pendingReport" to pendingCount,
            "completedReport" to completedCount
        )
    }

    override fun getRecentUserReports(limit: Int): List<Report> {
        return reportRepository.findAll()
            .sortedByDescending { it.createdAt }
            .take(limit)
    }

    override fun getAdminDashboardStats(): Map<String, Int> {
        val allReports = reportRepository.findAll()

        return mapOf(
            "pendingCount" to allReports.count { it.status == ReportStatus.PENDING },
            "inProgressCount" to allReports.count { it.status == ReportStatus.IN_PROGRESS },
            "completedCount" to allReports.count { it.status == ReportStatus.COMPLETED }
        )
    }

    override fun getPendingReports(): List<Report> {
        return reportRepository.findAll()
            .filter { it.status == ReportStatus.PENDING }
    }
}