package com.example.studentreport.seeder

import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional


@Component
class DatabaseSeeder(
    private val userSeeder: UserSeeder,
    private val categorySeeder: CategorySeeder,
    private val buildingSeeder: BuildingSeeder,
    private val roomSeeder: RoomSeeder,
    private val reportSeeder: ReportSeeder
) : CommandLineRunner {

    @Transactional
    override fun run(vararg args: String) {
        userSeeder.seed()
        categorySeeder.seed()
        buildingSeeder.seed()
        roomSeeder.seed()
        reportSeeder.seed()
    }
}