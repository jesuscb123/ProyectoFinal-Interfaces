package dam2.jetpack.proyectofinal.auth.domain.useCase

import dam2.jetpack.proyectofinal.auth.domain.model.AuthResult
import dam2.jetpack.proyectofinal.auth.domain.repository.AuthRepository
import dam2.jetpack.proyectofinal.user.domain.model.User
import dam2.jetpack.proyectofinal.user.domain.repository.UserRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(email: String, password: String): Result<AuthResult> {
        return try{
            require(email.isNotBlank()) {"El email no puede estar vacío"}
            require(password.isNotBlank()) {"La contraseña no puede estar vacía"}
            val auth = authRepository.register(email, password)
            Result.success(auth)
        }catch (e: IllegalArgumentException){
            Result.failure(e)
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}