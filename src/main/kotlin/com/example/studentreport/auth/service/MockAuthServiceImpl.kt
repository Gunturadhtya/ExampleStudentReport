package com.example.studentreport.auth.service

import com.example.studentreport.auth.dto.AuthResponse
import com.example.studentreport.auth.dto.LoginRequest
import com.example.studentreport.auth.dto.RegisterRequest
import com.example.studentreport.auth.dto.UserResponse
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

// This is only for Mocking not real service
@Service
@ConditionalOnProperty(name = ["app.security.mock-auth"], havingValue = "true", matchIfMissing = true)
class MockAuthServiceImpl: AuthService {
    private data class MockUserEntity(
        val profile: UserResponse,
        val passwordHash: String
    )

    private data class MockSessionEntity(
        val id: UUID,
        val userId: UUID,
        val token: String,
        val expiresAt: OffsetDateTime
    )

    private val mockUserDb = ConcurrentHashMap<String, MockUserEntity>()
    private val mockSessionDb = ConcurrentHashMap<String, MockSessionEntity>()

    override fun login(request: LoginRequest): AuthResponse {
        val entity = mockUserDb[request.email] ?: throw RuntimeException("User not found")

        if (entity.passwordHash != request.password) {
            throw RuntimeException("Invalid credentials")
        }

        val tokenString = UUID.randomUUID().toString()
        val expirationTime = OffsetDateTime.now().plusHours(1)

        val newSession = MockSessionEntity(
            id = UUID.randomUUID(),
            userId = entity.profile.id,
            token = tokenString,
            expiresAt = expirationTime
        )
        mockSessionDb[tokenString] = newSession

        return AuthResponse(
            token = "mock.jwt.token.${UUID.randomUUID()}",
            expiresAt = OffsetDateTime.now().plusHours(1),
            user = entity.profile
        )
    }

    override fun register(request: RegisterRequest): UserResponse {
        if (mockUserDb.containsKey(request.email)) {
            throw RuntimeException("User already registered")
        }

        val newUser = UserResponse(
            id = UUID.randomUUID(),
            email = request.email,
            name = request.name,
            role = "User",
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )

        val newEntity = MockUserEntity(
            profile = newUser,
            passwordHash = request.password
        )

        mockUserDb[request.email] = newEntity
        return newUser
    }

    override fun logout(token: String) {
        mockSessionDb.remove(token)
    }

    override fun validateSession(token: String): UserResponse? {
        val session = mockSessionDb[token] ?: return null

        if (session.expiresAt.isBefore(OffsetDateTime.now())) {
            mockSessionDb.remove(token)
            return null
        }

        return mockUserDb.values.firstOrNull { it.profile.id == session.userId }?.profile
    }

}