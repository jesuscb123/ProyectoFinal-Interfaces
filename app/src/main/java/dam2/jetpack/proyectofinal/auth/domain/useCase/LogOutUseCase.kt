package dam2.jetpack.proyectofinal.auth.domain.useCase

import dam2.jetpack.proyectofinal.auth.domain.repository.AuthRepository
import javax.inject.Inject

class LogOutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<Unit>{
        return try{
            val logOutIntent = authRepository.logOut()
            Result.success(logOutIntent)
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}