package dam2.jetpack.proyectofinal.user.domain.repository

import dam2.jetpack.proyectofinal.auth.domain.model.AuthResult
import dam2.jetpack.proyectofinal.user.domain.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz que define el contrato para el repositorio de datos de usuarios.
 *
 * Esta abstracción permite desacoplar la capa de dominio de la capa de datos,
 * especificando las operaciones que deben estar disponibles para la gestión de usuarios,
 * independientemente de si la fuente de datos es una base de datos local, una API remota, etc.
 */
interface UserRepository {
    /**
     * Obtiene un usuario por su ID único de Firebase.
     *
     * @param firebaseUid El UID de Firebase del usuario a buscar.
     * @return El objeto [User] correspondiente si se encuentra, o `null` en caso contrario.
     */
    suspend fun getUserByFirebaseUid(firebaseUid: String): User?

    /**
     * Guarda (inserta o actualiza) la información de un usuario.
     *
     * @param user El objeto [User] a guardar.
     */
    suspend fun saveUser(user: User)

    /**
     * Obtiene un usuario por su dirección de correo electrónico.
     *
     * @param email El email del usuario a buscar.
     * @return El objeto [User] correspondiente si se encuentra, o `null` en caso contrario.
     */
    suspend fun  getUserByEmail(email: String): User?

    /**
     * Obtiene una lista de todos los usuarios de la aplicación como un flujo de datos.
     *
     * @return Un [Flow] que emite una lista de [User] cada vez que los datos cambian.
     */
    suspend fun getAllUsers (): Flow<List<User>>

}
