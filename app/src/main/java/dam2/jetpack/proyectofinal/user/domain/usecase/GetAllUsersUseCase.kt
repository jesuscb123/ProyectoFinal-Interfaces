package dam2.jetpack.proyectofinal.user.domain.usecase

import dam2.jetpack.proyectofinal.user.domain.model.User
import dam2.jetpack.proyectofinal.user.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Caso de uso para obtener todos los usuarios registrados en el sistema.
 *
 * Esta clase encapsula la lógica para solicitar al [UserRepository] la lista completa
 * de usuarios. Al estar anotada con `@Inject`, puede ser fácilmente proporcionada
 * a otras clases, como ViewModels, que necesiten acceder a esta información.
 *
 * @property userRepository El repositorio que gestiona el acceso a los datos de los usuarios.
 */
class GetAllUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    /**
     * Ejecuta el caso de uso para obtener todos los usuarios.
     *
     * La sobrecarga del operador `invoke` permite llamar a la clase como si fuera una función
     * (ej: `getAllUsersUseCase()`).
     *
     * @return Un [Flow] que emite una lista de objetos [User] cada vez que los datos subyacentes cambian.
     */
    suspend operator fun invoke(): Flow<List<User>> {
        return userRepository.getAllUsers()

    }
}