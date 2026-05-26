package com.example.studentreport.web.service

import com.example.studentreport.entity.Category
import com.example.studentreport.entity.Room

interface MasterDataService {
    fun getAllCategories(): List<Category>
    fun getAllRooms(): List<Room>
    fun addCategory(name: String, description: String): Category
}