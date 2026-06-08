package com.example.studentreport.web.controller

import com.example.studentreport.user.service.UserService
import com.example.studentreport.web.service.WebAuthHelper
import org.springframework.data.domain.Pageable
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/admin/users")
class WebUserManagementController(
    private val userService: UserService,
    private val webAuthHelper: WebAuthHelper
) {
    @GetMapping
    fun userManagement(auth: Authentication?, model: Model): String {
        if (!webAuthHelper.isAdmin(auth)) {
            return "redirect:/dashboard"
        }

        val users = userService.getAllUsers(null, null, Pageable.ofSize(100)).content

        model.addAttribute("isAdmin", true)
        model.addAttribute("users", users)

        return "admin_users"
    }
}