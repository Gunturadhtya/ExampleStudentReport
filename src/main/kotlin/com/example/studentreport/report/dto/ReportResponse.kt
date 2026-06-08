package com.example.studentreport.report.dto

import com.example.studentreport.auth.dto.UserResponse
import com.example.studentreport.category.dto.CategoryResponse
import com.example.studentreport.entity.Category
import com.example.studentreport.entity.ReportStatus
import com.example.studentreport.entity.Room
import com.example.studentreport.room.dto.RoomResponse
import java.time.OffsetDateTime
import java.util.UUID

data class ReportResponse(
    val id: UUID,
    val version: Long,
    val reporter: UserResponse,
    val category: CategoryResponse,
    val room: RoomResponse,
    val categoryId: UUID,
    val roomId: UUID,
    val title: String,
    val description: String?,
    val status: ReportStatus,
    val upvoteCount: Int,
    val isUpvotedByMe: Boolean = false,
    val images: List<ReportImageResponse>,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
    val deletedAt: OffsetDateTime? = null
)