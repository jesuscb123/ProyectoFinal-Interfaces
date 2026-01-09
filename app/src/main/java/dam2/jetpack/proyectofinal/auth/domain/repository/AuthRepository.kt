package dam2.jetpack.proyectofinal.auth.domain.repository

import dam2.jetpack.proyectofinal.auth.domain.model.AuthResult


interface AuthRepository {

    suspend fun login(email: String, password: String): AuthResult

    suspend fun register(email: String, password: String): AuthResult

    suspend fun logOut()

    fun getCurrentUserUid(): String
}