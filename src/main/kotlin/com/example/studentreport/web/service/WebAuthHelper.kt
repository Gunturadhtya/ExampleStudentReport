package com.example.studentreport.web.service

import org.springframework.security.core.Authentication

interface WebAuthHelper {
    fun isAdmin(auth: Authentication?): Boolean
    fun isAuthenticated(auth: Authentication?): Boolean
    fun clearSessionCookie(): String
}