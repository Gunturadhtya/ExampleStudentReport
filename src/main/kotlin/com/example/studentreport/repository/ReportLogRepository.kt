package com.example.studentreport.repository

import com.example.studentreport.entity.ReportLog
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ReportLogRepository : JpaRepository<ReportLog, UUID> {
}