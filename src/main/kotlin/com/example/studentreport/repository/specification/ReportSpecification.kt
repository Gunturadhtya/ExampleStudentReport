package com.example.studentreport.repository.specification

import com.example.studentreport.entity.Report
import org.springframework.data.jpa.domain.Specification
import jakarta.persistence.criteria.Predicate
import java.time.Instant
import java.util.UUID


object ReportSpecification {
    fun withFeedFilters(search: String?, categoryId: UUID?, roomId: UUID?): Specification<Report> {
        return Specification { root, _, cb ->
            val predicates = mutableListOf<Predicate>()

            predicates.add(cb.isNull(root.get<Instant>("deletedAt")))

            if (!search.isNullOrBlank()) {
                val searchPattern = "%${search.lowercase()}%"
                val titlePredicate = cb.like(cb.lower(root.get("title")), searchPattern)
                val descPredicate = cb.like(cb.lower(root.get("description")), searchPattern)
                predicates.add(cb.or(titlePredicate, descPredicate))
            }

            if (categoryId != null) {
                predicates.add(cb.equal(root.get<UUID>("categoryId"), categoryId))
            }

            if (roomId != null) {
                predicates.add(cb.equal(root.get<UUID>("roomId"), roomId))
            }

            cb.and(*predicates.toTypedArray())
        }
    }
}