package com.example.studentreport.auth.controller

import com.example.studentreport.auth.dto.ApiResponse
import com.example.studentreport.auth.dto.AuthResponse
import com.example.studentreport.auth.dto.LoginRequest
import com.example.studentreport.auth.dto.RegisterRequest
import com.example.studentreport.auth.dto.UserResponse
import com.example.studentreport.auth.service.AuthService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.web.bind.annotation.CookieValue
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
    fun login(
        @RequestBody loginRequest: LoginRequest,
        response: HttpServletResponse
    ): ApiResponse<AuthResponse>{
        val authData = authService.login(loginRequest)

        val cookie = ResponseCookie.from("session_token", authData.token)
            .httpOnly(true)
            .secure(false)
            .path("/")
            .maxAge(3600)
            .sameSite("Strict")
            .build()

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString())

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
    fun logout(
        @CookieValue("session_token", required = false) cookieToken: String?,
        @RequestHeader("Authorization", required = false) headerToken: String?,
        response: HttpServletResponse
    ): ApiResponse<Unit> {
        val token = cookieToken ?: headerToken?.removePrefix("Bearer ")

        if (token != null) {
            authService.logout(token)
        }

        val cookie = ResponseCookie.from("session_token", "")
            .httpOnly(true)
            .path("/")
            .maxAge(0)
            .build()

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString())

        return ApiResponse(success = true, message = "Logout Successful")
    }
}