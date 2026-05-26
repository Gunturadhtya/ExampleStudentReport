package com.example.studentreport.web.controller

import com.example.studentreport.auth.service.AuthService
import com.example.studentreport.auth.service.ReportServiceImpl
import com.example.studentreport.repository.CategoryRepository
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.view.RedirectView
import com.example.studentreport.repository.ReportRepository
import com.example.studentreport.repository.RoomRepository
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import com.example.studentreport.entity.Category
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.time.Instant
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.CookieValue
import java.util.UUID

@Controller
class WebUIController (
    private val reportRepository: ReportRepository,
    private val reportService: ReportServiceImpl,
    private val categoryRepository: CategoryRepository,
    private val roomRepository: RoomRepository,
    private val authService: AuthService
) {
    private fun isAdmin(auth: Authentication?) : Boolean {
        return auth?.authorities?.any {
            it.authority == "ROLE_ADMIN" || it.authority == "ADMIN"
        } == true
    }

    private fun isAuthenticated(auth: Authentication?): Boolean {
        return auth?.isAuthenticated == true && auth.name != "anonymousUser"
    }

    private fun addCommonAttributes(model: Model, auth: Authentication?) {
        model.addAttribute("isAdmin", isAdmin(auth))
    }

    @GetMapping("/")
    fun rootRedirect(auth: Authentication?): RedirectView {
        if (isAuthenticated(auth)) {
            return if (isAdmin(auth)) RedirectView("/master-data") else RedirectView("/dashboard")
        }
        return RedirectView("/login")
    }

    @GetMapping("/login")
    fun login(auth: Authentication?): String {
        if (isAuthenticated(auth)) {
            return if (isAdmin(auth)) "redirect:/master-data" else "redirect:/dashboard"
        }

        return "login"
    }

    @GetMapping("/register")
    fun register(auth: Authentication?): String {
        if (isAuthenticated(auth)) {
            return if (isAdmin(auth)) "redirect:/master-data" else "redirect:/dashboard"
        }

        return "register"
    }

    @GetMapping("/user/logout")
    fun logout(
        @CookieValue("session_token", required = false) cookieToken: String?,
        request: HttpServletRequest,
        response: HttpServletResponse
    ): String {
        if (cookieToken != null) {
            authService.logout(cookieToken)
        }

        val cookie = ResponseCookie.from("session_token", "")
            .httpOnly(true)
            .path("/")
            .maxAge(0)
            .build()

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString())

        SecurityContextHolder.clearContext()
        request.session.invalidate()

        return "redirect:/login"
    }

    @GetMapping("/dashboard")
    fun dashboard(auth: Authentication?, model: Model): String {
        if (isAdmin(auth)) {
            return "redirect:/dashboard/admin"
        }

        addCommonAttributes(model, auth)

        val userReports = reportRepository.findAll()

        val pendingCount = userReports.count {
            it.status.name.equals("Pending", ignoreCase = true) || it.status.name.equals("In_Progress", ignoreCase = true)
        }
        val completedCount = userReports.count {
            it.status.name.equals("Completed", ignoreCase = true)
        }

        val userStats = mapOf(
            "reportCount" to userReports.size,
            "pendingReport" to pendingCount,
            "completedReport" to completedCount
        )

        model.addAttribute("userStats", userStats)
        model.addAttribute("recentReports", userReports.take(3))

        return "dashboard_mahasiswa"
    }

    @GetMapping("/dashboard/admin")
    fun dashboardAdmin(auth: Authentication?,model: Model): String {
        addCommonAttributes(model, auth)
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
    fun feed(
        @RequestParam(required = false) search: String?,
        @RequestParam(required = false) categoryId: UUID?,
        @RequestParam(required = false) roomId: UUID?,
        @PageableDefault(size = 20, sort = ["createdAt"], direction = Sort.Direction.DESC) pageable: Pageable,
        auth: Authentication?,
        model: Model
    ): String {
        addCommonAttributes(model, auth)

        val reportsPage = reportService.getFeedReports(search, categoryId, roomId, pageable)

        model.addAttribute("allReports", reportsPage.content)

        model.addAttribute("categories", categoryRepository.findAll())
        model.addAttribute("rooms", roomRepository.findAll())

        model.addAttribute("currentSearch", search)
        model.addAttribute("currentCategory", categoryId)
        model.addAttribute("currentRoom", roomId)

        return "feed"
    }

    @GetMapping("buat-laporan")
    fun buatLaporan(auth: Authentication?, model: Model) : String {
        addCommonAttributes(model, auth)
        return "buat_laporan"
    }

    @GetMapping("/master-data")
    fun masterData(auth: Authentication?, model: Model) : String {
        if (!isAdmin(auth)) {
            return "redirect:/dashboard"
        }

        addCommonAttributes(model, auth)
        model.addAttribute("categories", categoryRepository.findAll())
        model.addAttribute("rooms", roomRepository.findAll())
        return "master_data"
    }

    @PostMapping("/admin/categories")
    fun addCategory(@RequestParam name: String, @RequestParam description: String): String {
        val newCategory = Category(
            name = name,
            description = description,
            createdAt = Instant.now(),
            updatedAt = Instant.now()
        )
        categoryRepository.save(newCategory)
        return "redirect:/master-data"
    }
}