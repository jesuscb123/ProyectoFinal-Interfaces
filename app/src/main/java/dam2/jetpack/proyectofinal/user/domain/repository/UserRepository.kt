package dam2.jetpack.proyectofinal.user.domain.repository

import dam2.jetpack.proyectofinal.auth.domain.model.AuthResult
import dam2.jetpack.proyectofinal.user.domain.model.User

interface UserRepository {
    suspend fun getUserByFirebaseUid(firebaseUid: String): User
}