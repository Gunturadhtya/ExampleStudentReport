package com.example.studentreport.web.service.impl

import com.example.studentreport.entity.Category
import com.example.studentreport.entity.Room
import com.example.studentreport.repository.CategoryRepository
import com.example.studentreport.repository.RoomRepository
import com.example.studentreport.web.service.MasterDataService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class MasterDataServiceImpl(
    private val categoryRepository: CategoryRepository,
    private val roomRepository: RoomRepository
) : MasterDataService {

    @Transactional(readOnly = true)
    override fun getAllCategories(): List<Category> {
        return categoryRepository.findAll()
    }

    @Transactional(readOnly = true)
    override fun getAllRooms(): List<Room> {
        return roomRepository.findAll()
    }

    @Transactional
    override fun addCategory(name: String, description: String): Category {
        val newCategory = Category(
            name = name,
            description = description,
            createdAt = Instant.now(),
            updatedAt = Instant.now()
        )
        return categoryRepository.save(newCategory)
    }
}