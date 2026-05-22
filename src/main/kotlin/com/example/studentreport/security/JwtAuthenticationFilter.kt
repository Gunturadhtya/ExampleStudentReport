package com.example.studentreport.security

import org.springframework.web.filter.OncePerRequestFilter

abstract class JwtAuthenticationFilter: OncePerRequestFilter() {
}