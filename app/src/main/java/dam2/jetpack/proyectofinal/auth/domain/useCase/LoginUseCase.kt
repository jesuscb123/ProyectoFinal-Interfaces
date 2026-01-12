package dam2.jetpack.proyectofinal.auth.domain.useCase

import dam2.jetpack.proyectofinal.auth.domain.model.AuthResult
import dam2.jetpack.proyectofinal.auth.domain.repository.AuthRepository
import dam2.jetpack.proyectofinal.user.domain.model.User
import dam2.jetpack.proyectofinal.user.domain.repository.UserRepository

class LoginUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke (email: String, password: String): Result<User>{
        return try{
            val auth = authRepository.login(email, password)
            val user = userRepository.getUserByFirebaseUid(auth.uid)
            Result.success(user)
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}

