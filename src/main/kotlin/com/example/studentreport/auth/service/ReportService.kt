package com.example.studentreport.auth.service

import com.example.studentreport.entity.Report
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface ReportService {
    fun getFeedReports(search: String?, categoryId: UUID?, roomId: UUID?, pageable: Pageable): Page<Report>
}