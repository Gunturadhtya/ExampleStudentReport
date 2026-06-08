package com.example.studentreport.web.controller

import com.example.studentreport.auth.dto.UserResponse
import com.example.studentreport.user.service.UserService
import com.example.studentreport.web.service.WebAuthHelper
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import java.util.UUID

@Controller
class WebSettingsController(
    private val userService: UserService,
    private val webAuthHelper: WebAuthHelper
) {

    @GetMapping("/settings")
    fun settings(auth: Authentication?, model: Model): String {
        if (!webAuthHelper.isAuthenticated(auth)) {
            return "redirect:/login"
        }

        val userId = when (val principal = auth?.principal) {
            is UserResponse -> principal.id
            is UUID -> principal
            else -> return "redirect:/login"
        }

        val user = userService.getUserProfile(userId)
        val isAdmin = webAuthHelper.isAdmin(auth)

        model.addAttribute("user", user)
        model.addAttribute("isAdmin", isAdmin)

        if (!isAdmin) {
            try {
                val studentData = userService.getStudentData(userId)
                model.addAttribute("studentData", studentData)
            } catch (e: Exception) {
                model.addAttribute("studentData", null)
            }
        }

        return "settings"
    }
}