package com.example.studentreport.web.service

import com.example.studentreport.entity.Report

interface DashboardService {
    fun getUserDashboardStats(): Map<String, Int>
    fun getRecentUserReports(limit: Int): List<Report>
    fun getAdminDashboardStats(): Map<String, Int>
    fun getPendingReports(): List<Report>
}