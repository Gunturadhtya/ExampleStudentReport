package com.example.studentreport.web.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.view.RedirectView
import com.example.studentreport.repository.ReportRepository

@Controller
class WebUIController (
    private val reportRepository: ReportRepository
) {
    @GetMapping("/")
    fun rootRedirect(): RedirectView {
        return RedirectView("/login")
    }

    @GetMapping("/login")
    fun login() = "login"

    @GetMapping("/register")
    fun register() = "register"

    @GetMapping("/dashboard")
    fun dashboard() = "dashboard_mahasiswa"

    @GetMapping("/dashboard/admin")
    fun dashboardAdmin(model: Model): String {
        val allReports = reportRepository.findAll()

        val pendingReports = allReports.filter { it.status.name.equals("Pending", ignoreCase = true)}
        val inProgressCount = allReports.count { it.status.name.equals("In_Progress", ignoreCase = true)}
        val completedCount = allReports.count { it.status.name.equals("Completed", ignoreCase = true)}

        model.addAttribute("pendingCount", pendingReports.size)
        model.addAttribute("inProgressCount", inProgressCount)
        model.addAttribute("completedCount", completedCount)
        model.addAttribute("pendingReports", pendingReports)

        return "dashboard_admin"
    }

    @GetMapping("/feed")
    fun feed() = "feed"

    @GetMapping("buat-laporan")
    fun buatLaporan() = "buat_laporan"

    @GetMapping("/master-data")
    fun masterData() = "master_data"
}