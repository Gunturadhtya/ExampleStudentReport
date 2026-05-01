package com.example.studentreport

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
	fromApplication<StudentreportApplication>().with(TestcontainersConfiguration::class).run(*args)
}
