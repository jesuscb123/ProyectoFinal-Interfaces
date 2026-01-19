package dam2.jetpack.proyectofinal.user.domain.usecase

import dam2.jetpack.proyectofinal.user.domain.model.User
import dam2.jetpack.proyectofinal.user.domain.repository.UserRepository
import javax.inject.Inject

class GetUserByEmailUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String): Result<User?> {
        return try{
            val user = userRepository.getUserByEmail(email)
            Result.success(user)
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}