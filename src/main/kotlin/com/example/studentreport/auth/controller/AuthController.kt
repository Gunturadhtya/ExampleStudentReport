package com.example.studentreport.auth.controller

import com.example.studentreport.auth.dto.ApiResponse
import com.example.studentreport.auth.dto.AuthResponse
import com.example.studentreport.auth.dto.LoginRequest
import com.example.studentreport.auth.dto.RegisterRequest
import com.example.studentreport.auth.dto.UserResponse
import com.example.studentreport.auth.service.AuthService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ApiResponse<AuthResponse>{
        val authData = authService.login(loginRequest)
        return ApiResponse(success = true, message = "Login Successful", data = authData)
    }

    @PostMapping("/register")
    fun register(@RequestHeader("Idempotency-Key", required = false) idempotencyKey: String?,
                 @RequestBody registerRequest: RegisterRequest
    ): ApiResponse<UserResponse> {
        val userData = authService.register(registerRequest)
        return ApiResponse(success = true, message="Register Successful", data = userData)
    }

    @PostMapping("logout")
    fun logout(): ApiResponse<Unit> {
        return ApiResponse(success = true, message = "Logout Successful")
    }
}