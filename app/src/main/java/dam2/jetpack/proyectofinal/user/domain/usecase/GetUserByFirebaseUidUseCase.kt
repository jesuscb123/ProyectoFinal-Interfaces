package dam2.jetpack.proyectofinal.user.domain.usecase

import dam2.jetpack.proyectofinal.user.domain.model.User
import dam2.jetpack.proyectofinal.user.domain.repository.UserRepository
import javax.inject.Inject

class GetUserByFirebaseUidUseCase @Inject constructor(
    val userRepository: UserRepository
) {
    suspend operator fun invoke(firebaseUid: String): Result<User> {
        return try{
            val user = userRepository.getUserByFirebaseUid(firebaseUid)
            Result.success(user)
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}
