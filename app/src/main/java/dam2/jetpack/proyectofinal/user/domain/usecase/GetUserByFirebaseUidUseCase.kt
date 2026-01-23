package dam2.jetpack.proyectofinal.user.domain.usecase

import dam2.jetpack.proyectofinal.user.domain.model.User
import dam2.jetpack.proyectofinal.user.domain.repository.UserRepository
import javax.inject.Inject

/**
 * Caso de uso para obtener un usuario por su UID de Firebase.
 *
 * Esta clase encapsula la lógica para buscar un usuario a través del [UserRepository]
 * utilizando su UID de Firebase como identificador. Como caso de uso, representa una
 * única acción o funcionalidad dentro de la lógica de negocio de la aplicación.
 *
 * La clase utiliza la sobrecarga del operador `invoke` para poder ser llamada como si fuera una función.
 *
 * @param userRepository El repositorio que proporciona acceso a los datos de los usuarios.
 */
class GetUserByFirebaseUidUseCase @Inject constructor(
    val userRepository: UserRepository
) {
    /**
     * Ejecuta el caso de uso para buscar un usuario por su UID de Firebase.
     *
     * @param firebaseUid El UID de Firebase del usuario a buscar.
     * @return Un [Result] que encapsula el [User] si se encontró, o una excepción si ocurrió un error.
     */
    suspend operator fun invoke(firebaseUid: String): Result<User?> {
        return try{
            val user = userRepository.getUserByFirebaseUid(firebaseUid)
            Result.success(user)
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}
