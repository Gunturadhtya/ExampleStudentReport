package com.example.studentreport.web.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class WebUIController {
    @GetMapping("/login")
    fun login() = "login"

    @GetMapping("/register")
    fun register() = "register"

    @GetMapping("/dashboard")
    fun dashboard() = "dashboard_mahasiswa"
}