package dam2.jetpack.proyectofinal.user.domain.usecase

import dam2.jetpack.proyectofinal.user.domain.model.User
import dam2.jetpack.proyectofinal.user.domain.repository.UserRepository
import javax.inject.Inject

/**
 * Caso de uso para obtener un usuario por su dirección de correo electrónico.
 *
 * Esta clase encapsula la lógica para buscar un usuario a través del [UserRepository]
 * utilizando su email como identificador. Al ser un caso de uso, representa una
 * única acción o funcionalidad dentro de la lógica de negocio de la aplicación.
 *
 * La clase utiliza la sobrecarga del operador `invoke` para poder ser llamada como si fuera una función.
 *
 * @param userRepository El repositorio que proporciona acceso a los datos de los usuarios.
 */
class GetUserByEmailUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    /**
     * Ejecuta el caso de uso para buscar un usuario por su email.
     *
     * @param email El correo electrónico del usuario a buscar.
     * @return Un [Result] que encapsula el [User] si se encontró, o una excepción si ocurrió un error.
     */
    suspend operator fun invoke(email: String): Result<User?> {
        return try{
            val user = userRepository.getUserByEmail(email)
            Result.success(user)
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}
