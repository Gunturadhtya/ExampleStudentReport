package com.example.studentreport.seeder

import com.example.studentreport.entity.Category
import com.example.studentreport.repository.CategoryRepository
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class CategorySeeder(
    private val categoryRepository: CategoryRepository
) {
    fun seed() {
        if (categoryRepository.count() > 0) return

        val now = Instant.now()
        val categories = listOf(
            Category(
                name = "Infrastruktur",
                description = "Kerusakan fisik bangunan, atap, dinding, atau lantai",
                createdAt = now,
                updatedAt = now
            ),
            Category(name = "Kelistrikan", description = "Masalah listrik, lampu mati, atau stop kontak", createdAt = now, updatedAt = now),
            Category(name = "Fasilitas Kelas", description = "Meja, kursi, papan tulis, proyektor, atau AC", createdAt = now, updatedAt = now)
        )
        categoryRepository.saveAll(categories)
    }
}