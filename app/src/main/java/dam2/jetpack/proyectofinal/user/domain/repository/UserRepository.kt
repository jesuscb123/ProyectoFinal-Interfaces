package dam2.jetpack.proyectofinal.user.domain.repository

import dam2.jetpack.proyectofinal.auth.domain.model.AuthResult
import dam2.jetpack.proyectofinal.user.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUserByFirebaseUid(firebaseUid: String): User?
    suspend fun saveUser(user: User)

    suspend fun  getUserByEmail(email: String): User?

    suspend fun getAllUsers (): Flow<List<User>>

}