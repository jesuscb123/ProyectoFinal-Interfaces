package dam2.jetpack.proyectofinal.user.domain.usecase

import dam2.jetpack.proyectofinal.user.domain.model.User
import dam2.jetpack.proyectofinal.user.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Flow<List<User>> {
        return userRepository.getAllUsers()

    }
}