package com.example.studentreport.auth.service

import com.example.studentreport.entity.Report
import com.example.studentreport.repository.ReportRepository
import com.example.studentreport.repository.specification.ReportSpecification
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.awt.print.Pageable
import java.util.UUID

@Service
class ReportServiceImpl(
    private val reportRepository: ReportRepository
) : ReportService {

    @Transactional(readOnly = true)
    override fun getFeedReports(
        search: String?,
        categoryId: UUID?,
        roomId: UUID?,
        pageable: org.springframework.data.domain.Pageable
    ): Page<Report> {
        val spec = ReportSpecification.withFeedFilters(search, categoryId, roomId)
        return reportRepository.findAll(spec, pageable)
    }
}