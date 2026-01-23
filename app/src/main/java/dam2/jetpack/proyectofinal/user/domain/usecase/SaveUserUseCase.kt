package dam2.jetpack.proyectofinal.user.domain.usecase

import dam2.jetpack.proyectofinal.user.domain.model.User
import dam2.jetpack.proyectofinal.user.domain.repository.UserRepository
import javax.inject.Inject

/**
 * Caso de uso para guardar (insertar o actualizar) un usuario.
 *
 * Esta clase encapsula la lógica para persistir la información de un usuario
 * utilizando el [UserRepository]. Como caso de uso, representa una única
 * acción o funcionalidad dentro de la lógica de negocio de la aplicación.
 *
 * La clase utiliza la sobrecarga del operador `invoke` para poder ser llamada como si fuera una función.
 *
 * @param userRepository El repositorio que proporciona acceso a los datos de los usuarios.
 */
class SaveUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    /**
     * Ejecuta el caso de uso para guardar el usuario.
     *
     * @param user El objeto [User] que se va a guardar en la base de datos.
     */
    suspend operator fun invoke(user: User) {
        userRepository.saveUser(user)
    }
}

